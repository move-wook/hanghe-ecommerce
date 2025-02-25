package kr.hhplus.be.server.infra.outBox;

import kr.hhplus.be.server.domain.order.outbox.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaOrderOutBoxRepository extends JpaRepository<OrderOutbox, Long> {
    Optional<OrderOutbox> findByOrderId(long orderId);

    List<OrderOutbox> findAllByProcessedFalse();
}
