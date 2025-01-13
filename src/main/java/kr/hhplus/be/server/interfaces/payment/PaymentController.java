package kr.hhplus.be.server.interfaces.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.facade.PaymentFacade;
import kr.hhplus.be.server.support.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/v1")
@RestController
@Tag(name = "PaymentController", description = "결제")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @Operation(summary = "결제 요청", description = "사용자 특정 주문에 대한 결제를 요청")
    @PostMapping("/payments")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody PaymentRequest.PaymentRegisterV1  paymentRequest) {
        return ResponseBuilder.build(paymentFacade.processPayment(paymentRequest), HttpStatus.OK,"결제에 성공했습니다.");
    }
}
