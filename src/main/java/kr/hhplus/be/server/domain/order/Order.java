package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`order`") // 테이블명 명시
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING) // ENUM 타입 매핑
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 주문 상태 설정 메서드 (세터 대신 도메인 로직)
    public void updateStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("상태는 null일 수 없습니다.");
        }
        this.status = status;
    }

    public void validateNotCompleted() {
        if (this.status == OrderStatus.COMPLETED) {
            throw new HangHeaException(ErrorCode.ORDER_ALREADY_COMPLETED);
        }
    }

    public void assignUser(long userId) {
        this.userId = userId;
    }

    public void addTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static Order createOrder(Long userId, List<OrderItem> orderItems) {
        Order order = new Order();
        order.assignUser(userId);
        order.updateStatus(OrderStatus.PENDING);

        // 총 금액 계산
        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.addTotalPrice(totalPrice); // 총 금액 설정
        return order;
    }

}
