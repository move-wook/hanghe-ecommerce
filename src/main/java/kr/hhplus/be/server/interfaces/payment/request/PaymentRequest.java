package kr.hhplus.be.server.interfaces.payment.request;

import kr.hhplus.be.server.application.payment.request.PaymentInfo;

public class PaymentRequest {
    public record PaymentRegisterV1(long orderId, long userId, long issuedCouponId) {
        public static PaymentInfo.PaymentRegisterV1 from(PaymentRegisterV1 paymentRequest) {
            return new PaymentInfo.PaymentRegisterV1(paymentRequest.orderId(), paymentRequest.userId(), paymentRequest.issuedCouponId());
        }
    }
}
