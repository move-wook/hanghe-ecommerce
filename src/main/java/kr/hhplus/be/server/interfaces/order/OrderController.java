package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.facade.OrderFacade;
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
@Tag(name = "OrderController", description = "주문")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping("/orders")
    @Operation(summary = "주문 요청", description = "사용자 주문 요청")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest.OrderRegisterV1 orderRequest) {
        return ResponseBuilder.build(orderFacade.createOrder(orderRequest), HttpStatus.OK,"주문 생성에 성공했습니다.");
    }
}
