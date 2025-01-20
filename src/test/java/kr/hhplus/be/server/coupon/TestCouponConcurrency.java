package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.infra.coupon.JpaCouponRepository;
import kr.hhplus.be.server.support.HangHeaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class TestCouponConcurrency {

    @Autowired
    private JpaCouponRepository jpaCouponRepository;

    @Autowired
    private CouponService couponService;

    @BeforeEach
    void setup() {
        jpaCouponRepository.deleteAll();
    }


    @Test
    @DisplayName("최대 발급 가능한 쿠폰의 수의 사용자가 쿠폰을 발급한다.")
    void issueCouponMaxConcurrencyThrowsException() throws InterruptedException {
        // given
        Coupon coupon = Coupon.builder()
                .name("항해8기 수강 10%할인")
                .issuedCount(0)
                .limitCount(30)
                .validFrom(LocalDateTime.now())
                .validUntil(LocalDateTime.now().plusDays(10))
                .discountValue(BigDecimal.valueOf(10))
                .discountType("PERCENTAGE")
                .minimumOrderAmount(BigDecimal.valueOf(10))
                .build();
        jpaCouponRepository.save(coupon);

        int usersCount = 35;
        long couponId = coupon.getId();

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(usersCount);
        CountDownLatch latch = new CountDownLatch(usersCount);

        for (int i = 1; i <= usersCount; i++) {
            long user = i;
            executorService.execute(() -> {
                try {
                    couponService.issueCoupon(couponId, user);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });

        }

        latch.await();
        executorService.shutdown();

        // then
        Coupon issuanceCount = couponService.findByCouponId(couponId);
        assertThat(issuanceCount.getLimitCount()).isEqualTo(coupon.getLimitCount());

    }

    @Test
    @DisplayName("최대 발급 가능한 쿠폰의 수 이상에 사용자가 쿠폰을 발급하면 쿠폰 재고가 모두 소진된 예외를 발생시킨다.")
    void issueCouponMaxConcurrency() throws InterruptedException {
        Coupon coupon = Coupon.builder()
                .name("항해8기 수강 10%할인")
                .issuedCount(0)
                .limitCount(30) // 최대 30개 발급 가능
                .validFrom(LocalDateTime.now())
                .validUntil(LocalDateTime.now().plusDays(10))
                .discountValue(BigDecimal.valueOf(10))
                .discountType("PERCENTAGE")
                .minimumOrderAmount(BigDecimal.valueOf(10))
                .build();
        jpaCouponRepository.save(coupon);

        int usersCount = 35; // 30명 이상 발급 시도
        long couponId = coupon.getId();

        // 동시성 테스트를 위한 설정
        ExecutorService executorService = Executors.newFixedThreadPool(usersCount);
        CountDownLatch latch = new CountDownLatch(usersCount);

        List<Throwable> exceptions = new ArrayList<>(); // 발생한 예외를 수집

        for (int i = 1; i <= usersCount; i++) {
            long userId = i; // 고유 사용자 ID
            executorService.execute(() -> {
                try {
                    couponService.issueCoupon(couponId, userId); // 쿠폰 발급 시도
                } catch (Exception e) {
                    synchronized (exceptions) {
                        exceptions.add(e); // 예외 수집
                    }
                } finally {
                    latch.countDown(); // 완료 신호
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown(); // 스레드 풀 종료

        // then
        Coupon issuanceCount = couponService.findByCouponId(couponId);
        assertThat(issuanceCount.getIssuedCount()).isEqualTo(30); // 최대 발급 수 확인

        // 발생한 예외 검증
        long exceptionCount = exceptions.stream()
                .filter(e -> e instanceof HangHeaException) // 특정 예외 필터링
                .count();

        assertThat(exceptionCount).isEqualTo(5); // 초과 발급 시도한 5명에서 예외 발생

    }
}
