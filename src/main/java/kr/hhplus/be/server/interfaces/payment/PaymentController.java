package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.support.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/v1")
@RestController
public class PaymentController {

    @PostMapping("/payments")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody PaymentRequest paymentRequest) {
        return ResponseBuilder.build(Map.of(
                "resultCode", "S000",
                "status", "SUCCESS",
                "data", Map.of(
                                "paymentId", 789,
                                "orderId", 1,
                                "userId", 1,
                                "amount", 25000,
                                "status", "COMPLETED"
                        ),
                "message", "결제에 성공했습니다."
        ), HttpStatus.OK);
    }
}
