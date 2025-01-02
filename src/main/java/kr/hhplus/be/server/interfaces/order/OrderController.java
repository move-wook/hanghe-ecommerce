package kr.hhplus.be.server.interfaces.order;

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
public class OrderController {

    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseBuilder.build(Map.of(
                "resultCode", "S000",
                "status", "SUCCESS",
                "data", Map.of(
                "orderId", 456,
                "totalPrice", 3000,
                "status", "PENDING"
                ),
                "message", "주문 생성에 성공했습니다."
        ), HttpStatus.OK);
    }
}
