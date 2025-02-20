package kr.hhplus.be.server.domain.order.outbox;

import kr.hhplus.be.server.domain.order.event.OrderEvent;

import java.util.List;
import java.util.Optional;

public interface OrderOutboxRepository {
    void save(OrderOutbox orderOutbox);

    Optional<OrderOutbox> findById(long orderId);

    List<OrderOutbox> findAllByProcessedFalse();
}
