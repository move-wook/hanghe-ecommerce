package kr.hhplus.be.server.application.order.response;

public class OrderResult {
    public record OrderRegisterV1(long orderId, long totalPrice, String status){}
}
