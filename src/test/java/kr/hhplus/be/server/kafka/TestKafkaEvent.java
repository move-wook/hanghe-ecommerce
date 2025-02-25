package kr.hhplus.be.server.kafka;

import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.domain.order.event.OrderEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class TestKafkaEvent {
    private static final String TOPIC_NAME = "payment-completed-events";
    private static final AtomicReference<OrderEvent> receivedEvent = new AtomicReference<>();

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @KafkaListener(topics = TOPIC_NAME, groupId = "test-group")
    public void listen(OrderEvent event) {

        receivedEvent.set(event);
    }

    @Test
    void 결제완료_이벤트가_카프카를_통해_전달되는지_확인() {
        // given
        OrderEvent event = new OrderEvent(100L, 200L, 5000L, 300L);

        // when
        orderEventPublisher.publishPaymentEvent(event);

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> assertThat(receivedEvent.get()).isEqualTo(event));
    }
}
