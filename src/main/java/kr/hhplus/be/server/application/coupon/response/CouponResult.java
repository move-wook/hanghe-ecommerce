package kr.hhplus.be.server.application.coupon.response;

public class CouponResult {
    public record CouponRegisterV1(long issuedCouponId, String name,String status,String validUntil){
    }
    public record IssuedCouponRegisterV1(long issuedCouponId, long userId){}
}
