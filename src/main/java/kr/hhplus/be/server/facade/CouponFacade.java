package kr.hhplus.be.server.facade;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.interfaces.coupon.CouponRequest;
import kr.hhplus.be.server.interfaces.coupon.CouponResponse;
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
    public CouponResponse.IssuedCouponRegisterV1 issueCoupon(CouponRequest.CouponRegisterV1 couponRequest) {
        //사용자 검증
        User user = userService.getUserById(couponRequest.userId());
        // 쿠폰 발급
        IssuedCoupon issuedCoupon = couponService.issueCoupon(couponRequest.couponId(), user);

        return new CouponResponse.IssuedCouponRegisterV1(issuedCoupon.getId(),
                user.getId());
    }

    public List<CouponResponse.CouponRegisterV1> getUserCoupons(long userId) {
        User user = userService.getUserById(userId);
        List<IssuedCoupon> coupons =  couponService.getByUserId(user);
        return toCouponRegisterV1List(coupons);
    }
    private List<CouponResponse.CouponRegisterV1> toCouponRegisterV1List(List<IssuedCoupon> issuedCoupons) {
        return issuedCoupons.stream()
                .map(this::toCouponRegisterV1)
                .collect(Collectors.toList());
    }

    private CouponResponse.CouponRegisterV1 toCouponRegisterV1(IssuedCoupon issuedCoupon) {
        Coupon coupon = issuedCoupon.getNonNullCoupon();

        return new CouponResponse.CouponRegisterV1(
                issuedCoupon.getId(),
                coupon.getName(),
                issuedCoupon.getStatus(),
                coupon.getFormattedValidUntil()
        );
    }


}
