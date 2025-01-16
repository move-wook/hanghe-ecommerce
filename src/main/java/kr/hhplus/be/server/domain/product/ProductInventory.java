package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_inventory")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private long stock;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public void validateStock(long quantity) {
        if (this.stock < quantity) {
            throw new HangHeaException(ErrorCode.PRODUCT_EXPIRED);
        }
    }

    public void deductStock(long quantity) {
        validateStock(quantity);
        subtractStock(quantity);
    }

    public void subtractStock(long quantity) {
        this.stock -= quantity;
    }
}