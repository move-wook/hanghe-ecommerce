package kr.hhplus.be.server.interfaces.coupon.reqeust;

import kr.hhplus.be.server.application.coupon.request.CouponInfo;

public class CouponRequest {
    public record CouponUserInfo(long userId){
        public static CouponInfo.UserCouponRegisterV1 from(long userId) {
            return new CouponInfo.UserCouponRegisterV1(userId);
        }
    }
    public record IssuedCoupon(
            long userId,
            long couponId) {
        public static CouponInfo.CouponRegisterV1 from(CouponRequest.IssuedCoupon couponRequest) {
            return new CouponInfo.CouponRegisterV1(couponRequest.userId(), couponRequest.couponId());
        }
    }
}
