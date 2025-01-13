package kr.hhplus.be.server.domain.coupon;


import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<IssuedCoupon> getByUserId(User user) {
        return couponRepository.findAllByUserId(user.getId())
                .orElseThrow(() -> new HangHeaException(ErrorCode.NOT_FOUND_USER_COUPON));
    }
    @Transactional
    public Coupon findCouponForUpdate(long couponId) {
        return couponRepository.findCouponForUpdate(couponId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.INVALID_COUPON));
    }
    @Transactional
    public IssuedCoupon issueCoupon(long couponId, User user) {
        Coupon coupon = this.findCouponForUpdate(couponId);

        if (!coupon.canIssue()) {
            throw new HangHeaException(ErrorCode.COUPON_EXPIRED);
        }
        coupon.incrementIssuedCount();
        couponRepository.save(coupon);

        IssuedCoupon issuedCoupon = new IssuedCoupon(user, coupon);
        couponRepository.save(issuedCoupon);  // 발급된 쿠폰 저장

        return issuedCoupon; // 발급된 쿠폰 반환
    }
    @Transactional
    public void useCoupon(long issueCouponId, long UserId) {
        // 발급된 쿠폰을 찾아오기
        IssuedCoupon issuedCoupon = this.findByIdAndUserId(issueCouponId, UserId);

        // 쿠폰이 이미 사용된 경우 예외 처리
        if (issuedCoupon.isUsed()) {
            throw new HangHeaException(ErrorCode.COUPON_ALREADY_USED);
        }

        // 쿠폰이 만료된 경우 예외 처리
        if (issuedCoupon.isExpired()) {
            throw new HangHeaException(ErrorCode.COUPON_EXPIRED);
        }

        // 쿠폰 사용 처리
        issuedCoupon.markAsUsed();  // 쿠폰 상태, 시간를 USED로 변경
        couponRepository.save(issuedCoupon);  // 사용된 쿠폰 업데이트

    }

    public IssuedCoupon findByIdAndUserId(long couponId, long userId) {
        return couponRepository.findByIdAndUserId(couponId, userId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.NOT_FOUND_USER_COUPON));
    }
}
