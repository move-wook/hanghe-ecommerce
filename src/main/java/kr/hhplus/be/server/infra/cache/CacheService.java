package kr.hhplus.be.server.infra.cache;

import kr.hhplus.be.server.infra.order.ProductTopResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public List<ProductTopResult> getTopSellingProductsFromCache(String key) {
        return (List<ProductTopResult>) redisTemplate.opsForValue().get(key);
    }

    public void saveTopSellingProductsToCache(String key, List<ProductTopResult> products) {
        redisTemplate.opsForValue().set(key, products, Duration.ofMinutes(1)); // TTL 1분 설정
    }

    public void refreshCacheTTL(String key) {
        redisTemplate.expire(key, Duration.ofMinutes(1)); // TTL 갱신
    }

    public void refreshTopSellingProductsCache(String key, List<ProductTopResult> updatedProducts) {
        // 캐시 삭제 (캐시를 삭제하고 새로 저장)
        redisTemplate.delete(key);

        // 갱신된 데이터를 다시 캐시로 저장
        redisTemplate.opsForValue().set(key, updatedProducts, Duration.ofMinutes(1));  // TTL 1분 설정
    }
}