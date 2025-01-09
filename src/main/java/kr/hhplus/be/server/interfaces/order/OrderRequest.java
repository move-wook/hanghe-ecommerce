package kr.hhplus.be.server.interfaces.order;

import java.util.List;

public class OrderRequest {
    public record OrderRegisterV1(long userId, List<OrderProductRegisterV1> orderItems) {}
    public record OrderProductRegisterV1(long productId, long quantity) {}
}
