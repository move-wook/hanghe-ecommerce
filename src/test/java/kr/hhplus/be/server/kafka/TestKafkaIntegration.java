package kr.hhplus.be.server.kafka;


import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class TestKafkaIntegration {

    private static final String TOPIC_NAME = "test-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final AtomicReference<String> receivedMessage = new AtomicReference<>();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = TOPIC_NAME, groupId = "test-group")
    public void listen(String message) {
        log.info("✅ Received message: {}", message);
        receivedMessage.set(message);
    }

    @Test
    void testKafkaProducerConsumer() {
        // 메시지 전송
        kafkaTemplate.send(TOPIC_NAME, "Hello Kafka!");
        log.info("✅ Kafka message sent");

        // waitility를 이용해 메시지 도착을 기다림 (최대 5초)
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> assertThat(receivedMessage.get()).isEqualTo("Hello Kafka!"));
    }
}
