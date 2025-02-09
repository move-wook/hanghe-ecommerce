package kr.hhplus.be.server.infra.coupon;

import kr.hhplus.be.server.domain.coupon.CouponRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ICouponRedisRepository implements CouponRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    @Override
    public void issueCouponInRedis(long couponId, long userId, long requestTime) {
        String request = couponId + ":" + userId;
        redisTemplate.opsForZSet().add("coupon:queue", request, requestTime);
    }
}
