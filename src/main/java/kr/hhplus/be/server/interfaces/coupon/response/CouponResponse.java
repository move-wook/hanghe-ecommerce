package kr.hhplus.be.server.interfaces.coupon.response;

import kr.hhplus.be.server.application.coupon.response.CouponResult;

import java.util.List;
import java.util.stream.Collectors;

public class CouponResponse {
    public record CouponRegisterV1(long issuedCouponId, String name,String status,String validUntil){

        public static List<CouponRegisterV1> of(List<CouponResult.CouponRegisterV1> list) {
            return list.stream()
                    .map(coupon -> new CouponRegisterV1(
                            coupon.issuedCouponId(),
                            coupon.name(),
                            coupon.status(),
                            coupon.validUntil()))
                    .collect(Collectors.toList());
        }
    }
    public record IssuedCouponRegisterV1(long issuedCouponId, long userId){
        public static CouponResult.IssuedCouponRegisterV1 of(CouponResult.IssuedCouponRegisterV1 couponResponse) {
            return new CouponResult.IssuedCouponRegisterV1(
                    couponResponse.issuedCouponId(), couponResponse.userId()
            );
        }
    }
}
