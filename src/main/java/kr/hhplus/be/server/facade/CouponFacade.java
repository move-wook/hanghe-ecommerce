package kr.hhplus.be.server.facade;

import jakarta.validation.constraints.NotNull;
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

import java.time.format.DateTimeFormatter;
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
        return coupons.stream()
                .map(this::setCouponRegisterV1)
                .collect(Collectors.toList());
    }

    private CouponResponse.CouponRegisterV1 setCouponRegisterV1(IssuedCoupon issuedCoupon) {
        return getCouponRegisterV1(issuedCoupon);

    }

    @NotNull
    static CouponResponse.CouponRegisterV1 getCouponRegisterV1(IssuedCoupon issuedCoupon) {
        Coupon coupon = issuedCoupon.getCoupon();

        if (coupon == null) {
            throw new IllegalStateException("Coupon not found for IssuedCoupon ID: " + issuedCoupon.getId());
        }

        String status = issuedCoupon.isUsed() ? "USED" : "UNUSED";
        String validUntil = coupon.getValidUntil() != null
                ? coupon.getValidUntil().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : "N/A";

        return new CouponResponse.CouponRegisterV1(coupon.getId(),
                coupon.getName(),
                status,
                validUntil);
    }


}
