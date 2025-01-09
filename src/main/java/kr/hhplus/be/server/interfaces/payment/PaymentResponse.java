package kr.hhplus.be.server.interfaces.payment;

public class PaymentResponse {
    public record PaymentRegisterV1(long paymentId, long orderId, long amount, String status) {}

}
