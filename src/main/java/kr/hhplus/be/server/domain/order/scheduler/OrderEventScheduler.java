package kr.hhplus.be.server.domain.order.scheduler;

import kr.hhplus.be.server.domain.order.outbox.OrderOutbox;
import kr.hhplus.be.server.domain.order.outbox.OrderOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventScheduler {

    private final OrderOutboxService orderOutboxService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC_NAME = "payment-completed-events";

    @Scheduled(cron = "0 */5 * * * *")
    public void rePublishFailedEvents() {
        List<OrderOutbox> unprocessedEvents = orderOutboxService.getUnprocessedEvents();
        for (OrderOutbox event : unprocessedEvents) {
            kafkaTemplate.send(TOPIC_NAME, event.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("✅ 재발송 성공: {}", event.getPayload());
                            orderOutboxService.markAsProcessed(event.getOrderId());
                        }});
        }
    }
}
