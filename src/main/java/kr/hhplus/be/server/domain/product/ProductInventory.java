package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private long stock;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    // 비즈니스 로직
    public void addStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Quantity to add cannot be negative");
        }
        this.stock += amount;
    }

    public void subtractStock(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity to subtract cannot be negative");
        }
        if (this.stock < quantity) {
            throw new IllegalStateException("Not enough stock available");
        }
        this.stock -= quantity;
    }
}