package kr.hhplus.be.server.application.payment.response;

public class PaymentResult {
    public record PaymentRegisterV1(long paymentId, long amount,  String status) {}

}
