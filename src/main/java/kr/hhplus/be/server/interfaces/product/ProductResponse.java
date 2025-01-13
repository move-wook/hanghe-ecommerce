    package kr.hhplus.be.server.interfaces.product;

    import kr.hhplus.be.server.domain.product.Product;
    import lombok.Getter;

    import java.math.RoundingMode;

    @Getter
    public class ProductResponse {
        public record ProductRegisterV1(Long id, String name, long price, String description, long stock) {

        }

        public  ProductRegisterV1 from(Product product) {
            return new ProductRegisterV1(
                    product.getId(),
                    product.getName(),
                    product.getPrice().setScale(0, RoundingMode.DOWN).longValue(),
                    product.getDescription(),
                    product.getProductInventory().getStock()
            );
        }

        public record ProductTopRegisterV1(long productId, String name, long totalSold){}
    }
