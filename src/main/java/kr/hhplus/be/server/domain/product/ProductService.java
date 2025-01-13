package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(long productId) {
        return productRepository.getProduct(productId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.NOT_FOUND_PRODUCT));
    }
    public Page<Product> listProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    @Transactional
    public ProductInventory getProductInventoryForUpdate(long productId) {
        return productRepository.getProductInventoryForUpdate(productId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.NOT_FOUND_PRODUCT));
    }
    @Transactional
    public void decrementStock(Long productId, long quantity) {
        ProductInventory inventory = this.getProductInventoryForUpdate(productId);
        inventory.subtractStock(quantity);  // 재고 차감 로직 호출
        productRepository.saveProductInventory(inventory);  // 차감된 재고 정보 저장
    }
}
