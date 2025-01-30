package kr.hhplus.be.server.interfaces.payment.response;

import kr.hhplus.be.server.application.payment.response.PaymentResult;

public class PaymentResponse {
    public record PaymentRegisterV1(long paymentId, long amount,  String status){
        public static PaymentResponse.PaymentRegisterV1 of(PaymentResult.PaymentRegisterV1 registerV1) {
            return new PaymentResponse.PaymentRegisterV1(registerV1.paymentId(), registerV1.amount(), registerV1.status());
        }
    }
}
