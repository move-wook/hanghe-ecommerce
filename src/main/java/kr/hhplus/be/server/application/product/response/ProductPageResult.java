package kr.hhplus.be.server.application.product.response;

import java.util.List;


public class ProductPageResult {
    public record ProductPageRegisterV1(
            List<ProductResult.ProductRegisterV1> products,
            int totalPages,
            long totalElements,
            int currentPage
    ) {}
    public ProductPageRegisterV1 from(List<ProductResult.ProductRegisterV1> products, int totalPages, long totalElements, int currentPage) {
        return new ProductPageRegisterV1(products, totalPages, totalElements, currentPage);
    }
}
