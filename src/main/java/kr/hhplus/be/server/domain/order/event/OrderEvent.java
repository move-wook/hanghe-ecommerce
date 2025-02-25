package kr.hhplus.be.server.domain.order.event;

public record OrderEvent(long orderId, long userId, long totalAmount, long paymentId){}
