package kr.hhplus.be.server.domain.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponProcessorScheduler {

    private final CouponProcessorService couponProcessorService;
    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(fixedRate = 1000)  // 1초마다 실행
    public void processCoupons() {
        // 비동기 방식으로 발급 작업을 처리
        Set<ZSetOperations.TypedTuple<String>> requests = redisTemplate.opsForZSet().popMin("coupon:queue", 10);
        if (requests != null && !requests.isEmpty()) {
            for (ZSetOperations.TypedTuple<String> tuple : requests) {
                String request = tuple.getValue(); // 쿠폰 요청 데이터 가져오기

                // 쿠폰 ID와 사용자 ID를 분리
                String[] requestData = request.split(":");
                long couponId = Long.parseLong(requestData[0]);
                long userId = Long.parseLong(requestData[1]);

                // 비동기 방식으로 발급 작업을 처리
                CompletableFuture<Void> future = couponProcessorService.processCouponRequests(couponId, userId);

                // 비동기 작업 완료 후 처리
                future.thenRun(() -> {redisTemplate.opsForZSet().remove("coupon:queue", request);});
            }
        }
    }
}
