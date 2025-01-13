package kr.hhplus.be.server.infra.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaIssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {
    Optional<List<IssuedCoupon>> findAllByUserId(long id);
    @Query("SELECT b FROM IssuedCoupon b WHERE b.id = :id AND b.user.id = :userId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<IssuedCoupon> findByIdAndUserId(@Param("id") long id, @Param("userId")long userId);
}
