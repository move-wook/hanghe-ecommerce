package kr.hhplus.be.server.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInventory;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestProductService {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 id 로 특정 상품의 정보를 반환한다.")
    void shouldReturnProductWhenProductExists() {
        // Given
        long productId = 1L;
        Product mockProduct = Product.builder()
                .id(productId)
                .name("티셔츠")
                .price(BigDecimal.valueOf(100))
                .build();
        when(productRepository.getProduct(productId)).thenReturn(Optional.of(mockProduct));

        // When
        Product result = productService.getProductById(productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo("티셔츠");

    }

    @Test
    @DisplayName("특정 상품이 존재하지 않는 경우")
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        long productId = 1L;
        when(productRepository.getProduct(productId)).thenReturn(Optional.empty());

        // When & Then
        HangHeaException exception = assertThrows(HangHeaException.class, () -> {
            productService.getProductById(productId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_PRODUCT);
        assertThat(exception.getMessage()).isEqualTo("해당 상품이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상품의 재고를 조회한다.")
    void shouldReturnProductInventoryForUpdate() {
        // Given
        long productId = 1L;
        ProductInventory mockInventory = ProductInventory.builder()
                .productId(productId)
                .id(productId)
                .stock(100)
                .build();
        when(productRepository.getProductInventoryForUpdate(productId)).thenReturn(Optional.of(mockInventory));

        // When
        ProductInventory result = productService.getProductInventoryForUpdate(productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(productId);
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getStock()).isEqualTo(100);

    }

    @Test
    @DisplayName("특정 상품의 재고를 감소시킨다.")
    void shouldDecrementStockSuccessfully() {
        // Given
        long productId = 1L;
        long quantity = 10;

        ProductInventory mockInventory = ProductInventory.builder()
                .productId(productId)
                .id(productId)
                .stock(100)
                .build();
        when(productRepository.getProductInventoryForUpdate(productId)).thenReturn(Optional.of(mockInventory));

        // When
        productService.decrementStock(productId, quantity);

        // Then
        assertThat(mockInventory.getStock()).isEqualTo(90); // Check stock updated

    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void shouldReturnPagedProductsWhenProductsExist() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        Product product1 = Product.builder()
                .id(1L)
                .name("티셔츠")
                .price(BigDecimal.valueOf(100))
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("원피스")
                .price(BigDecimal.valueOf(200))
                .build();

        Page<Product> mockPage = new PageImpl<>(Arrays.asList(product1, product2));
        when(productRepository.findAll(pageable)).thenReturn(mockPage);

        // When
        Page<Product> result = productService.listProducts(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo("티셔츠");
        assertThat(result.getContent().get(1).getName()).isEqualTo("원피스");
    }




}
