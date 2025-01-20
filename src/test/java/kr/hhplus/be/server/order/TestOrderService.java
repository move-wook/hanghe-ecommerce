package kr.hhplus.be.server.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.order.ProductTopResult;
import kr.hhplus.be.server.application.order.request.OrderInfo;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestOrderService {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("정상적으로 주문을 요청한다")
    void shouldCreateOrderSuccessfully() {
        // Given
        User user = User.builder().id(1L).build();

        Product product1 = Product.builder().id(1L).name("티셔츠").price(BigDecimal.valueOf(100)).build();
        Product product2 = Product.builder().id(2L).name("원피스").price(BigDecimal.valueOf(200)).build();

        OrderInfo.OrderProductRegisterV1 orderProduct1 = new OrderInfo.OrderProductRegisterV1(1L, 2);
        OrderInfo.OrderProductRegisterV1 orderProduct2 = new OrderInfo.OrderProductRegisterV1(2L, 1);

        List<Product> products = Arrays.asList(product1, product2);
        List<OrderInfo.OrderProductRegisterV1> orderProducts = Arrays.asList(orderProduct1, orderProduct2);

        Order order = new Order();
        order.assignUser(user.getId());
        order.updateStatus(OrderStatus.PENDING);

        doNothing().when(orderRepository).save(any(Order.class));

        // When
        Order result = orderService.processOrderCreation(user, products, orderProducts);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(user.getId());
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);

    }

    @Test
    @DisplayName("존재하지 않는 주문 번호 조회시 HangHeaException 예외 발생")
    void shouldThrowExceptionWhenOrderNotFoundForUpdate() {
        // Given
        long orderId = 1L;
        when(orderRepository.findOrderForUpdate(orderId)).thenReturn(Optional.empty());

        // When & Then
        HangHeaException exception = assertThrows(HangHeaException.class, () -> {
            orderService.findOrderForUpdate(orderId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND);

    }

    @Test
    @DisplayName("주문정보를 정상적으로 저장한다.")
    void shouldSaveOrderSuccessfully() {
        // Given
        Order order = new Order();

        // When
        orderService.updateStatus(order);

        // Then
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("가장 많이 팔린 상위 상품을 주문과 상품을 통해 3순위까지 조회한다.")
    void shouldFindTopSellingProductsSuccessfully() {
        // Given
        int limit = 5;
        ProductTopResult topResult1 = new ProductTopResult(1L, "Product A", 100);
        ProductTopResult topResult2 = new ProductTopResult(2L, "Product B", 50);

        when(orderRepository.findTopSellingProducts(limit)).thenReturn(Arrays.asList(topResult1, topResult2));

        // When
        List<ProductTopResult> result = orderService.findTopSellingProducts(limit);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(1).id()).isEqualTo(2L);

        verify(orderRepository, times(1)).findTopSellingProducts(limit);
    }

}
