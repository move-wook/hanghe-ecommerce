package kr.hhplus.be.server.domain.coupon;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponProcessorService {

    private static final String COUPON_KEY = "issued_coupons";
    private static final String COUPON_STOCK_KEY = "coupon_stock";
    private static final String COUPON_QUEUE_KEY = "coupon:queue";

    private final CouponRepository couponRepository;

    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void processCouponRequests(long couponId, long userId) {
        String stockKey = COUPON_STOCK_KEY + ":" + couponId;
        Long stock = redisTemplate.opsForValue().decrement(stockKey);
        if (stock == null || stock < 0) {
            redisTemplate.opsForValue().increment(stockKey); // 롤백
            log.debug("쿠폰 재고 부족");
            return;
        }

        String userKey = COUPON_QUEUE_KEY + couponId + ":" + userId;
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(COUPON_KEY, userKey))) {
            redisTemplate.opsForValue().increment(stockKey); // 롤백
            log.debug("이미 쿠폰을 발급받은 사용자");
            return;
        }

        Coupon coupon = couponRepository.findByCouponId(couponId)
                .orElseThrow(() -> new RuntimeException("쿠폰 정보 없음"));

        coupon.incrementIssuedCount();
        couponRepository.save(coupon);

        IssuedCoupon issuedCoupon = new IssuedCoupon(userId, couponId);
        couponRepository.save(issuedCoupon);

        redisTemplate.opsForSet().add(COUPON_KEY, userKey);
        redisTemplate.expire(COUPON_KEY, 24, TimeUnit.HOURS);
        log.info("쿠폰이 발급되었습니다: couponId={}, userId={}", couponId, userId);
    }
}
