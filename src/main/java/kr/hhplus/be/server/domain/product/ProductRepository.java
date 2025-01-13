package kr.hhplus.be.server.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> getProduct(long productId);

    Page<Product> findAll(Pageable pageable);

    Optional<ProductInventory> getProductInventoryForUpdate(long productId);

    void saveProductInventory(ProductInventory productInventory);
}
