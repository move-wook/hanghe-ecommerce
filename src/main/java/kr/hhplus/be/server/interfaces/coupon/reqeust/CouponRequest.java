package kr.hhplus.be.server.interfaces.coupon.reqeust;

public class CouponRequest {
    public record CouponInfo(long userId){
    }
    public record IssuedCoupon(
            long userId,
            long couponId) {

    }
}
