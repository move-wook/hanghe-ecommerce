package kr.hhplus.be.server.infra.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaCouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c WHERE c.id = :couponId AND c.issuedCount < c.limitCount")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Coupon> findCouponForUpdate(@Param("couponId")long couponId);

    Optional<Coupon> findById(@Param("couponId")long couponId);
}
