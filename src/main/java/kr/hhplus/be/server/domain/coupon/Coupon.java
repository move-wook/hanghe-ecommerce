package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String discountType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal minimumOrderAmount;

    @Column(nullable = false)
    private int limitCount;

    @Column(nullable = false)
    private int issuedCount;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    // 비즈니스 로직
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(validFrom) && now.isBefore(validUntil);
    }

    public void incrementIssuedCount() {
        //쿠폰 재고 체크
        if (issuedCount >= limitCount) {
            throw new HangHeaException(ErrorCode.COUPON_EXPIRED);
        }
        this.issuedCount++;
    }

    public String getFormattedValidUntil() {
        return validUntil != null
                ? validUntil.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : "N/A";
    }




}
