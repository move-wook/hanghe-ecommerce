package kr.hhplus.be.server.domain.order.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.order.outbox.OrderOutbox;
import kr.hhplus.be.server.domain.order.outbox.OrderOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final OrderOutboxService orderOutboxService;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private static final String TOPIC_NAME = "payment-completed-events";
    private final ObjectMapper objectMapper; // JSON 변환을 위해 필요

    // outbox 저장
    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void saveOutbox(OrderEvent event) {
        try {
            String eventPayload = objectMapper.writeValueAsString(event); // JSON 변환

            OrderOutbox outbox = OrderOutbox.builder()
                    .orderId(event.orderId())
                    .eventType("PAYMENT_COMPLETED")
                    .payload(eventPayload)
                    .processed(false)
                    .build();

            orderOutboxService.save(outbox);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event", e);
        }
    }

    // 메시지 발행
    // 메세지 발행후 처리 상태 변경
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void publishPaymentEvent(OrderEvent event) {
        kafkaTemplate.send(TOPIC_NAME, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("✅ Kafka 발행 성공: {}", event);
                        orderOutboxService.markAsProcessed(event.orderId());
                    }
                });
    }

}
