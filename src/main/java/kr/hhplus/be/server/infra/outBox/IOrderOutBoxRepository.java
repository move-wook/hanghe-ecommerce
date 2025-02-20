package kr.hhplus.be.server.infra.outBox;

import kr.hhplus.be.server.domain.order.outbox.OrderOutbox;
import kr.hhplus.be.server.domain.order.outbox.OrderOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IOrderOutBoxRepository implements OrderOutboxRepository {

    private final JpaOrderOutBoxRepository jpaOrderOutBoxRepository;

    @Override
    public void save(OrderOutbox orderOutbox) {
        jpaOrderOutBoxRepository.save(orderOutbox);
    }

    @Override
    public Optional<OrderOutbox> findById(long orderId) {
        return jpaOrderOutBoxRepository.findByOrderId(orderId);
    }

    @Override
    public List<OrderOutbox> findAllByProcessedFalse() {
        return jpaOrderOutBoxRepository.findAllByProcessedFalse();
    }
}
