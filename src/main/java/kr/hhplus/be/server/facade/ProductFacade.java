package kr.hhplus.be.server.facade;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.interfaces.product.ProductPageResponse;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
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

    // 리스트 조회
    public ProductPageResponse.ProductPageRegisterV1 listProducts(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]));
        Page<Product> productPage = productService.listProducts(pageable);

        List<ProductResponse.ProductRegisterV1> products = productPage.getContent().stream()
                .map(new ProductResponse()::from)
                .toList();

        return new ProductPageResponse().from(
                products,
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.getNumber()
        );
    }

    // 단건 조회
    public ProductResponse.ProductRegisterV1 getProductById(Long id) {
        Product product = productService.getProductById(id);
        return new ProductResponse().from(product);
    }

    public List<ProductResponse.ProductTopRegisterV1> getTopProductsBySales(int limit) {
        return orderService.findTopSellingProducts(limit).stream()
                .map(result -> new ProductResponse.ProductTopRegisterV1(
                        result.id(),
                        result.name(),
                        result.totalSold()
                ))
                .toList();
    }
}
