package kr.hhplus.be.server.application.payment.request;

public class PaymentInfo {
    public record PaymentRegisterV1(long orderId, long userId, long issuedCouponId) {}
}
