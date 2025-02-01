package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.application.coupon.request.CouponInfo;
import kr.hhplus.be.server.application.coupon.response.CouponResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponService couponService;
    private final UserService userService;

    @Transactional
    public CouponResult.IssuedCouponRegisterV1 issueCoupon(CouponInfo.CouponRegisterV1 couponRegisterV1) {
        //사용자 검증
        User user = userService.getUserById(couponRegisterV1.userId());
        // 쿠폰 발급
        IssuedCoupon issuedCoupon = couponService.issueCoupon(couponRegisterV1.couponId(), user.getId());

        return new CouponResult.IssuedCouponRegisterV1(issuedCoupon.getId(),
                user.getId());
    }

    public List<CouponResult.CouponRegisterV1> getUserCoupons(CouponInfo.UserCouponRegisterV1 userCouponRegisterV1) {
        User user = userService.getUserById(userCouponRegisterV1.userId());
        List<IssuedCoupon> coupons =  couponService.getByUserId(user.getId());
        return toCouponRegisterV1List(coupons);
    }
    private List<CouponResult.CouponRegisterV1> toCouponRegisterV1List(List<IssuedCoupon> issuedCoupons) {
        return issuedCoupons.stream()
                .map(this::toCouponRegisterV1)
                .collect(Collectors.toList());
    }

    private CouponResult.CouponRegisterV1 toCouponRegisterV1(IssuedCoupon issuedCoupon) {
        Coupon coupon = couponService.findByCouponId(issuedCoupon.getCouponId());
        return new CouponResult.CouponRegisterV1(
                issuedCoupon.getId(),
                coupon.getName(),
                issuedCoupon.getStatus(),
                coupon.getFormattedValidUntil()
        );
    }


}
