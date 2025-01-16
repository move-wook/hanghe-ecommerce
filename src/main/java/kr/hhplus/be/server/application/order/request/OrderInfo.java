package kr.hhplus.be.server.application.order.request;

import java.util.List;

public class OrderInfo {
    public record OrderRegisterV1(long userId, List<OrderProductRegisterV1> orderItems) {}
    public record OrderProductRegisterV1(long productId, long quantity) {}
}
