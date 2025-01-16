package kr.hhplus.be.server.interfaces.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.application.payment.request.PaymentInfo;
import kr.hhplus.be.server.application.payment.response.PaymentResult;
import kr.hhplus.be.server.support.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@Tag(name = "PaymentController", description = "결제")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @Operation(summary = "결제 요청", description = "사용자 특정 주문에 대한 결제를 요청")
    @PostMapping("/payments")
    public CustomApiResponse<PaymentResult.PaymentRegisterV1> processPayment(@RequestBody PaymentInfo.PaymentRegisterV1  paymentRequest){
        return CustomApiResponse.ok(paymentFacade.processPayment(paymentRequest),"결제에 성공했습니다.");
    }
}
