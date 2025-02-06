package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "issued_coupon")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class IssuedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long couponId;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    private String status;

    @Column
    private LocalDateTime usedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 발급 시 생성자
    public IssuedCoupon(Long userId, Long couponId) {
        this.userId = userId;
        this.couponId = couponId;
        this.used = false;  // 초기값: 사용되지 않음
        this.usedAt = null; // 사용된 날짜는 없음
        this.status = "PENDING";
    }

    // 비즈니스 로직
    public void markAsUsed(Coupon coupon) {
        //쿠폰을 이미 사용한 경우
        if(this.isUsed()){
            throw new HangHeaException(ErrorCode.COUPON_ALREADY_USED);
        }
        //쿠폰이 만료된 경우
        if(this.isExpired(coupon)){
            throw new HangHeaException(ErrorCode.COUPON_EXPIRED);
        }

        this.used = true;
        this.usedAt = LocalDateTime.now();
    }

    public boolean isExpired(Coupon coupon) {
        return !coupon.isValid();
    }

    public String getStatus() {
        return used ? "USED" : "UNUSED";
    }


}
