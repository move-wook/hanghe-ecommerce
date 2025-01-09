package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestCouponService {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    @DisplayName("사용자 id로 쿠폰 발급 목록을 조회한다.")
    void shouldReturnCouponsForUser() {
        // Given
        User user = User.builder().id(1L).build();

        IssuedCoupon issuedCoupon = new IssuedCoupon(user, new Coupon());

        when(couponRepository.findAllByUserId(user.getId())).thenReturn(Optional.of(Collections.singletonList(issuedCoupon)));

        // When
        List<IssuedCoupon> result = couponService.getByUserId(user);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("없는 사용자 조회시 쿠폰 사용자를 찾을 없는 예외를 발생시킨다.")
    void shouldThrowExceptionWhenNoCouponsForUser() {
        // Given
        User user = User.builder().id(1L).build();
        when(couponRepository.findAllByUserId(user.getId())).thenReturn(Optional.empty());

        // When & Then
        HangHeaException exception = assertThrows(HangHeaException.class, () -> {
            couponService.getByUserId(user);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER_COUPON);

    }

    @Test
    @DisplayName("쿠폰조회시 비관적 락을 건다")
    void shouldFindCouponForUpdate() {
        // Given
        long couponId = 1L;
        Coupon coupon = new Coupon();
        when(couponRepository.findCouponForUpdate(couponId)).thenReturn(Optional.of(coupon));

        // When
        Coupon result = couponService.findCouponForUpdate(couponId);

        // Then
        assertThat(result).isNotNull();

    }

    @Test
    @DisplayName("쿠폰 id가 유효하지 않을 경우 HangHeaException 예외를 발생 시킨다.")
    void shouldThrowExceptionWhenCouponNotFoundForUpdate() {
        // Given
        long couponId = 1L;
        when(couponRepository.findCouponForUpdate(couponId)).thenReturn(Optional.empty());

        // When & Then
        HangHeaException exception = assertThrows(HangHeaException.class, () -> {
            couponService.findCouponForUpdate(couponId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_COUPON);

    }

    @Test
    @DisplayName("쿠폰을 정상적으로 발급 시킨다.")
    void shouldIssueCouponSuccessfully() {
        // Given

        long couponId = 1L;

        // 사용자와 쿠폰 객체를 빌더로 생성
        User user = User.builder().id(1L).build();
        Coupon coupon = Coupon.builder()
                .id(couponId)
                .issuedCount(1)
                .limitCount(10)
                .validFrom(LocalDateTime.now().minusDays(1))
                .validUntil(LocalDateTime.now().plusDays(1))
                .build();

        // Mocking findCouponForUpdate 메서드
        when(couponRepository.findCouponForUpdate(couponId)).thenReturn(Optional.of(coupon));

        // Mocking save 메서드 (void 처리)
        doNothing().when(couponRepository).save(any(Coupon.class));
        doNothing().when(couponRepository).save(any(IssuedCoupon.class));

        // When
        IssuedCoupon result = couponService.issueCoupon(couponId, user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getCoupon()).isEqualTo(coupon);


    }

    @Test
    @DisplayName("쿠폰 발급시 재고가 없면 HangHeaException예외를 발생시킨다.")
    void shouldThrowExceptionWhenCouponCannotBeIssued() {
        // Given
        long couponId = 1L;
        User user = User.builder().id(1L).build();
        Coupon coupon = Coupon.builder()
                .id(1L)
                .issuedCount(10)
                .limitCount(10)
                .validFrom(LocalDateTime.now().minusDays(1))
                .validUntil(LocalDateTime.now().plusDays(1))
                .build();

        when(couponRepository.findCouponForUpdate(couponId)).thenReturn(Optional.of(coupon));

        // When & Then
        HangHeaException exception = assertThrows(HangHeaException.class, () -> {
            couponService.issueCoupon(couponId, user);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.COUPON_EXPIRED);

    }

    @Test
    @DisplayName("쿠폰을 정상적으로 사용 처리한다.")
    void shouldUseCouponSuccessfully() {
        // Given
        long issueCouponId = 1L;
        long userId = 1L;
        Coupon coupon = Coupon.builder()
                .id(1L)
                .issuedCount(1)
                .limitCount(10)
                .validFrom(LocalDateTime.now().minusDays(1))
                .validUntil(LocalDateTime.now().plusDays(1))
                .build();
        IssuedCoupon issuedCoupon = IssuedCoupon.builder().coupon(coupon)
                        .used(false).build();
        when(couponRepository.findByIdAndUserId(issueCouponId, userId)).thenReturn(Optional.of(issuedCoupon));

        // When
        couponService.useCoupon(issueCouponId, userId);

        // Then
        assertThat(issuedCoupon.isUsed()).isTrue();

    }

    @Test
    @DisplayName("이미 상용된 쿠폰에 대해 HangHeaException 예외를 발생 시킨다.")
    void shouldThrowExceptionWhenUsingAlreadyUsedCoupon() {
        // Given
        long issueCouponId = 1L;
        long userId = 1L;
        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .used(true).build();
        when(couponRepository.findByIdAndUserId(issueCouponId, userId)).thenReturn(Optional.of(issuedCoupon));

        // When & Then
        HangHeaException exception = assertThrows(HangHeaException.class, () -> {
            couponService.useCoupon(issueCouponId, userId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.COUPON_ALREADY_USED);

    }

}
