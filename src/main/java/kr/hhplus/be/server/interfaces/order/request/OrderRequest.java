package kr.hhplus.be.server.interfaces.order.request;

import kr.hhplus.be.server.application.order.request.OrderInfo;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    public record CreateOrder(long userId, List<OrderRequest.OrderProductRegisterV1> orderItems){
        public static OrderInfo.OrderRegisterV1 from(CreateOrder order) {
            // OrderProductRegisterV1 리스트를 변환
            List<OrderInfo.OrderProductRegisterV1> orderProductList = order.orderItems.stream()
                    .map(orderProduct -> new OrderInfo.OrderProductRegisterV1(orderProduct.productId(), orderProduct.quantity()))
                    .collect(Collectors.toList());

            // OrderRegisterV1 객체 생성 후 반환
            return new OrderInfo.OrderRegisterV1(order.userId(), orderProductList);
        }

    }
    public record OrderProductRegisterV1(long productId, long quantity) {}
}
