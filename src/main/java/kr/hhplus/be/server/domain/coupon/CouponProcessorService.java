package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponProcessorService {

    private final CouponRepository couponRepository;

    private final RedisTemplate<String, String> redisTemplate;

    @Async  // 비동기적으로 실행
    public CompletableFuture<Void> processCouponRequests(long couponId, long userId) {
        // 발급된 쿠폰 수 가져오기

        Long issuedCount = redisTemplate.opsForValue().increment("coupon:issuedCount", 1);

        // 최대 발급 수 초과 체크
        if (issuedCount > 30) {
            log.debug("발급 수량 초과");
            return CompletableFuture.completedFuture(null);
        }

        // 쿠폰 발급 처리
        Coupon coupon = couponRepository.findByCouponId(couponId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.INVALID_COUPON));

        coupon.incrementIssuedCount();
        couponRepository.save(coupon);

        // 발급된 쿠폰 저장
        IssuedCoupon issuedCoupon = new IssuedCoupon(userId, coupon.getId());
        couponRepository.save(issuedCoupon);

        // 발급 완료
        return CompletableFuture.completedFuture(null);
    }
}
