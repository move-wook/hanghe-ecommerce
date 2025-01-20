package kr.hhplus.be.server.infra.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInventory;
import kr.hhplus.be.server.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IProductRepository implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final JpaProductInventoryRepository jpaProductInventoryRepository;

    @Override
    public Optional<Product> getProduct(long productId) {
        return jpaProductRepository.findById(productId);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaProductRepository.findAll(pageable);
    }

    @Override
    public Optional<ProductInventory> getProductInventoryForUpdate(long productId) {
        return jpaProductInventoryRepository.findProductInventoryForUpdate(productId);
    }

    @Override
    public void saveProductInventory(ProductInventory productInventory) {
         jpaProductInventoryRepository.save(productInventory);
    }

    @Override
    public Optional<ProductInventory> getProductInventory(long productId) {
        return jpaProductInventoryRepository.findByProductId(productId);
    }
}
