package kr.hhplus.be.server.interfaces.product;

import java.util.List;


public class ProductPageResponse {
    public record ProductPageRegisterV1(
            List<ProductResponse.ProductRegisterV1> products,
            int totalPages,
            long totalElements,
            int currentPage
    ) {}
    public ProductPageRegisterV1 from(List<ProductResponse.ProductRegisterV1> products, int totalPages, long totalElements, int currentPage) {
        return new ProductPageRegisterV1(products, totalPages, totalElements, currentPage);
    }
}
