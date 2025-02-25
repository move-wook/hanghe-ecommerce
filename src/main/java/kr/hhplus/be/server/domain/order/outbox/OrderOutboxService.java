package kr.hhplus.be.server.domain.order.outbox;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderOutboxService {

    private final OrderOutboxRepository orderOutboxRepository;

    public void save(OrderOutbox orderOutbox) {
        orderOutboxRepository.save(orderOutbox);
    }

    @Transactional
    public void markAsProcessed(Long orderId) {
        Optional<OrderOutbox> outbox = orderOutboxRepository.findById(orderId);
        outbox.ifPresent(event -> {
            event.markAsProcessed();
            orderOutboxRepository.save(event);
        });
    }

    public List<OrderOutbox> getUnprocessedEvents() {
        return orderOutboxRepository.findAllByProcessedFalse();
    }
}
