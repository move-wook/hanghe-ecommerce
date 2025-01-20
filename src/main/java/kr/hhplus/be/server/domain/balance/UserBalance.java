package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_balance")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    // 수정 시점 업데이트
    @PrePersist
    public void prePersist() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public void addBalance(BigDecimal newBalance) {
        BigDecimal MAX_BALANCE_LIMIT = BigDecimal.valueOf(10000000);
        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            //충전 금액이 0보다 작거나 같을 경우
            throw new HangHeaException(ErrorCode.INVALID_BALANCE);
        }
        if(currentBalance.add(newBalance).compareTo(MAX_BALANCE_LIMIT) > 0) {
            throw new HangHeaException(ErrorCode.MAX_BALANCE);
        }
        this.currentBalance = currentBalance.add(newBalance);
        this.lastUpdated = LocalDateTime.now(); // 잔액 수정 시간 업데이트
    }

    public void subBalance(BigDecimal subBalance) {
        if (this.getCurrentBalance().compareTo(subBalance) < 0) {
            throw new HangHeaException(ErrorCode.INSUFFICIENT_BALANCE);  // 잔액 부족 예외 처리
        }
        this.currentBalance =  this.getCurrentBalance().subtract(subBalance);
    }



}
