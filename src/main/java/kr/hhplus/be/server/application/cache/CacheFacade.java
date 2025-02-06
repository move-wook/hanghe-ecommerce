package kr.hhplus.be.server.application.cache;

import kr.hhplus.be.server.application.cache.request.CacheInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.infra.cache.CacheService;
import kr.hhplus.be.server.infra.order.ProductTopResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CacheFacade {

    private final CacheService cacheService;

    private final OrderService orderService;

    public void warmUpCache(CacheInfo.CacheRegisterV1 request){
        int limit = 5;
        List<ProductTopResult> list = orderService.findTopSellingProducts(limit);
        String key = request.key() + "::" + limit;
        cacheService.saveTopSellingProductsToCache(key, list);
    }


}
