package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // 총 금액 계산 메서드
    public BigDecimal calculateTotalPrice() {
        if (price == null || quantity <= 0) {
            throw new IllegalStateException("Invalid price or quantity");
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }
    // 생성자 및 비즈니스 로직
    public OrderItem(Long productId, long quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public void assignOrder(Long orderId) {
        this.orderId = orderId;
    }


}
