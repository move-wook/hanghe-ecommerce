package kr.hhplus.be.server.order;

import kr.hhplus.be.server.application.product.ProductFacade;
import kr.hhplus.be.server.application.product.response.ProductResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class TestCacheOrderService {

    @Autowired
    private ProductFacade productFacade;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("상위 상품 목록을 캐시를 사용한 조회.")
    void shouldTopProductsExist() {
        int limit = 5;
        String cacheKey = "topSellingProducts::" + limit;
        // 첫 번째 호출: DB에서 호출이 이루어짐
        List<ProductResult.ProductTopRegisterV1> result = productFacade.getTopProductsBySales(limit);

        // 첫 번째 호출 후 캐시가 저장되었는지 확인
        assertThat(redisTemplate.opsForValue().get(cacheKey)).isNotNull();

        // 첫 번째 호출 후 TTL 값 확인 (초 단위)
        Long ttlBefore = redisTemplate.getExpire(cacheKey);
        assertThat(ttlBefore).isGreaterThan(0);  // TTL이 0초 이상이어야 함

    }
}
