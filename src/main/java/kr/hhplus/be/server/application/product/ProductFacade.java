package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInventory;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.application.product.response.ProductPageResult;
import kr.hhplus.be.server.application.product.response.ProductResult;
import kr.hhplus.be.server.infra.cache.CacheService;
import kr.hhplus.be.server.infra.order.ProductTopResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;
    private final OrderService orderService;
    private final CacheService cacheService;

    // 리스트 조회
    public ProductPageResult.ProductPageRegisterV1 listProducts(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]));
        Page<Product> productPage = productService.listProducts(pageable);

        List<ProductResult.ProductRegisterV1> products = productPage.getContent().stream()
                .map(product -> {
                    long stock = productService.getProductInventory(product.getId()).getStock();
                    return ProductResult.ProductRegisterV1.from(product, stock);
                })
                .toList();

        return new ProductPageResult().from(
                products,
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.getNumber()
        );
    }

    // 단건 조회
    public ProductResult.ProductRegisterV1 getProductById(Long id) {
        Product product = productService.getProductById(id);
        ProductInventory productInventory = productService.getProductInventory(id);
        return ProductResult.ProductRegisterV1.from(product, productInventory.getStock());
    }

    public List<ProductResult.ProductTopRegisterV1> getTopProductsBySales(int limit) {
        String key = "topSellingProducts::" + limit;
        List<ProductTopResult> topSellingProducts = cacheService.getTopSellingProductsFromCache(key);
        if (topSellingProducts == null) {
            topSellingProducts = orderService.findTopSellingProducts(limit);
            cacheService.saveTopSellingProductsToCache(key, topSellingProducts);
        } else {
            cacheService.refreshCacheTTL(key);
        }
        return orderService.findTopSellingProducts(limit).stream()
                .map(result -> new ProductResult.ProductTopRegisterV1(
                        result.id(),
                        result.name(),
                        result.totalSold()
                ))
                .toList();
    }
}
