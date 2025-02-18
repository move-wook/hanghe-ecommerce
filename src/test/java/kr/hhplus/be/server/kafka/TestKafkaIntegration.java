package kr.hhplus.be.server.kafka;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class TestKafkaIntegration {

    private static final String TOPIC_NAME = "test-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String receivedMessage;

    @KafkaListener(topics = TOPIC_NAME, groupId = "kafka-group")
    public void listen(String message) {
        receivedMessage = message;
    }

    @Test
    void testKafkaProducerConsumer() throws InterruptedException {
        // Kafka에 메시지 전송
        kafkaTemplate.send(TOPIC_NAME, "Hello Kafka!");

        // Consumer가 메시지를 받았는지 확인 (비동기 테스트)
        Thread.sleep(2000); // 약간의 대기 시간 필요
        log.info("received message: {}", receivedMessage);
        assertThat(receivedMessage).isEqualTo("Hello Kafka!");
    }

}
