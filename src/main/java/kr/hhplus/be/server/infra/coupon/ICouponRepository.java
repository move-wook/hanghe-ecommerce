package kr.hhplus.be.server.infra.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ICouponRepository implements CouponRepository {

    private final JpaCouponRepository jpaCouponRepository;
    private final JpaIssuedCouponRepository jpaIssuedCouponRepository;

    @Override
    public Optional<Coupon> findCouponForUpdate(long couponId) {
        return jpaCouponRepository.findCouponForUpdate(couponId);
    }

    @Override
    public void save(Coupon coupon) {
        jpaCouponRepository.save(coupon);
    }

    @Override
    public Optional<List<IssuedCoupon>> findAllByUserId(long id) {
        return jpaIssuedCouponRepository.findAllByUserId(id);
    }

    @Override
    public void save(IssuedCoupon issuedCoupon) {
         jpaIssuedCouponRepository.save(issuedCoupon);
    }

    @Override
    public Optional<IssuedCoupon> findByIdAndUserId(long couponId, long userId) {
        return jpaIssuedCouponRepository.findByIdAndUserId(couponId, userId);
    }

}
