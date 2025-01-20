package kr.hhplus.be.server.domain.coupon;



import java.util.List;
import java.util.Optional;

public interface CouponRepository {

    Optional<Coupon> findCouponForUpdate(long couponId);

    void save(Coupon coupon);

    Optional<List<IssuedCoupon>> findAllByUserId(long id);

    void save(IssuedCoupon issuedCoupon);

    Optional<IssuedCoupon> findByIdAndUserId(long couponId, long userId);

    Optional<Coupon> findByCouponId(long couponId);
}
