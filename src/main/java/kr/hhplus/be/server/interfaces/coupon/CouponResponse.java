package kr.hhplus.be.server.interfaces.coupon;

public class CouponResponse {
    public record CouponRegisterV1(long couponId, String name,String status,String validUntil){}
    public record IssuedCouponRegisterV1(long couponId, long userId){}
}
