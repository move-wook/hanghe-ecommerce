package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public ProductInventory getProductInventory(long productId) {
        return productRepository.getProductInventory(productId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.NOT_FOUND_PRODUCT));
    }
    @Transactional
    public void decrementStock(long productId, long quantity) {
        ProductInventory inventory = this.getProductInventoryForUpdate(productId);
        inventory.subtractStock(quantity);  // 재고 차감 로직 호출
        productRepository.saveProductInventory(inventory);  // 차감된 재고 정보 저장
    }
    @Transactional
    public void validateAndDeductStock(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            ProductInventory inventory = getProductInventoryForUpdate(orderItem.getProductId());
            inventory.deductStock(orderItem.getQuantity()); // 엔티티의 메서드 호출
            productRepository.saveProductInventory(inventory); // 차감된 재고 저장
        }
    }
    @Transactional
    public void validateStockAvailability(long productId, long quantity)  {
        ProductInventory inventory = getProductInventoryForUpdate(productId);
        inventory.validateStock(quantity); // 재고 검증만 수행
    }
}
