package kr.hhplus.be.server.kafka;


import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class TestKafkaIntegration {

    private static final String TOPIC_NAME = "test-topic";
    private static final LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = TOPIC_NAME, groupId = "test-group")
    public void listen(String message) {

        messageQueue.offer(message);
    }

    @Test
    void testKafkaProducerConsumer() throws InterruptedException {
        // 메시지 전송
        kafkaTemplate.send(TOPIC_NAME, "Hello Kafka!");


        // 메시지 수신 대기
        String receivedMessage = messageQueue.poll(5, TimeUnit.SECONDS);
        assertThat(receivedMessage).isEqualTo("Hello Kafka!");
    }
}
