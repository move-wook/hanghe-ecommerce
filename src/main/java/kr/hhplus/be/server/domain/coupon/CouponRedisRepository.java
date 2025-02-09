package kr.hhplus.be.server.domain.coupon;

public interface CouponRedisRepository {
    void issueCouponInRedis(long couponId, long userId, long requestTime);
}
