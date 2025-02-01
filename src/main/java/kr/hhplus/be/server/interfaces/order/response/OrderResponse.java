package kr.hhplus.be.server.interfaces.order.response;

import kr.hhplus.be.server.application.order.response.OrderResult;

public class OrderResponse {
    public record OrderRegisterV1(long orderId, long totalPrice, String status){
        public static OrderRegisterV1 of(OrderResult.OrderRegisterV1 orderRegisterV1) {
            return new OrderRegisterV1(orderRegisterV1.orderId(), orderRegisterV1.totalPrice(), orderRegisterV1.status());
        }
    }
}
