package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.User;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(nullable = false)
    private boolean used;

    @Column
    private LocalDateTime usedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 발급 시 생성자
    public IssuedCoupon(User user, Coupon coupon) {
        this.user = user;
        this.coupon = coupon;
        this.used = false;  // 초기값: 사용되지 않음
        this.usedAt = null; // 사용된 날짜는 없음
    }

    // 비즈니스 로직
    public void markAsUsed() {
        //쿠폰을 이미 사용한 경우
        if(this.isUsed()){
            throw new HangHeaException(ErrorCode.COUPON_ALREADY_USED);
        }
        //쿠폰이 만료된 경우
        if(this.isExpired()){
                throw new HangHeaException(ErrorCode.COUPON_EXPIRED);
        }

        this.used = true;
        this.usedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return !coupon.isValid();
    }
}
