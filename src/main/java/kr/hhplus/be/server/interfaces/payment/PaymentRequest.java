package kr.hhplus.be.server.interfaces.payment;

public class PaymentRequest {
    public record PaymentRegisterV1(long orderId, long userId, long issuedCouponId) {}
}
