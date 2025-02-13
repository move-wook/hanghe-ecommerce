package kr.hhplus.be.server.external;

public record PaymentCompletedEvent(long orderId, long userId, long totalAmount, long paymentId) {}
