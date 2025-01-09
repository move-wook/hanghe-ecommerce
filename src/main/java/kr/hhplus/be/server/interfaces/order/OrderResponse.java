package kr.hhplus.be.server.interfaces.order;

public class OrderResponse {
    public record OrderRegisterV1(long orderId, long totalPrice, String status){}
}
