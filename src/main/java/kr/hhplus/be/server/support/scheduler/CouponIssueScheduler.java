package kr.hhplus.be.server.support.scheduler;

import kr.hhplus.be.server.domain.coupon.CouponProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponIssueScheduler {

    private final RedisTemplate<String, String> redisTemplate;
    private final CouponProcessorService couponProcessorService;

    private static final String COUPON_QUEUE_KEY = "coupon:queue";

    @Scheduled(fixedRate = 1000)
    public void processCoupons() {
        Set<ZSetOperations.TypedTuple<String>> requests = redisTemplate.opsForZSet().popMin(COUPON_QUEUE_KEY, 10);
        if (requests != null && !requests.isEmpty()) {
            for (ZSetOperations.TypedTuple<String> request : requests) {
                String requestValue = request.getValue();
                if (requestValue != null) {
                    String[] requestData = requestValue.split(":");
                    long couponId = Long.parseLong(requestData[0]);
                    long userId = Long.parseLong(requestData[1]);
                    couponProcessorService.processCouponRequests(couponId, userId);
                }
            }
        }
    }

}
