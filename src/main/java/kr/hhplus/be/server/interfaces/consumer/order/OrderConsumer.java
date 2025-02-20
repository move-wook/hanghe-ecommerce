package kr.hhplus.be.server.interfaces.consumer.order;

import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.external.EcommerceDataPlatform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final EcommerceDataPlatform ecommerceDataPlatform;

    @KafkaListener(topics = "payment-completed-events", groupId = "payment-service")
    public void handlePaymentEvent(ConsumerRecord<String, OrderEvent> record) {
        OrderEvent event = record.value();
        log.info("✅ Kafka 이벤트 수신: {}", event);
        ecommerceDataPlatform.send(event);
    }
}
