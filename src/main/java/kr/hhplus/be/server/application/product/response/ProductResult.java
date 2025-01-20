    package kr.hhplus.be.server.application.product.response;

    import kr.hhplus.be.server.domain.product.Product;
    import lombok.Getter;

    import java.math.RoundingMode;

    @Getter
    public class ProductResult {
        public record ProductRegisterV1(Long id, String name, long price, String description, long stock) {
            public static ProductRegisterV1 from(Product product, long stock) {
                return new ProductRegisterV1(
                        product.getId(),
                        product.getName(),
                        product.getPrice().setScale(0, RoundingMode.DOWN).longValue(),
                        product.getDescription(),
                        stock
                );
            }
        }



        public record ProductTopRegisterV1(long productId, String name, long totalSold){}
    }
