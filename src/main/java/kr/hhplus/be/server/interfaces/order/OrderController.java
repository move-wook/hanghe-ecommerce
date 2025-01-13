package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.facade.OrderFacade;
import kr.hhplus.be.server.support.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@Tag(name = "OrderController", description = "주문")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping("/orders")
    @Operation(summary = "주문 요청", description = "사용자 주문 요청")
    public CustomApiResponse<OrderResponse.OrderRegisterV1> createOrder(@RequestBody OrderRequest.OrderRegisterV1 orderRequest) {
        return CustomApiResponse.ok(orderFacade.createOrder(orderRequest), "주문 생성에 성공했습니다.");
    }
}
