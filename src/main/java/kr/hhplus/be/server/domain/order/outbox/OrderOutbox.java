package kr.hhplus.be.server.domain.order.outbox;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_outbox")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long orderId;

    private String eventType;
    @Lob
    private String payload;  // JSON 형태로 저장

    private LocalDateTime createdAt;

    private Boolean processed;

    public void markAsProcessed() {
        this.processed = true;
    }
}
