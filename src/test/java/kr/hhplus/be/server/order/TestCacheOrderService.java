package kr.hhplus.be.server.order;

import kr.hhplus.be.server.application.product.ProductFacade;
import kr.hhplus.be.server.domain.order.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
public class TestCacheOrderService {

    @Autowired
    private OrderService orderService;

    @MockitoSpyBean
    private OrderRepository orderRepository;

    @Autowired
    private ProductFacade productFacade;

    @Test
    @DisplayName("상위 상품 목록을 캐시를 사용한 조회.")
    void shouldTopProductsExist() {
        // given
        List<OrderItem> orderItems = Arrays.asList(
                new OrderItem(1L, 2, new BigDecimal("15000.00")) // 상품 101, 2개, 개당 15,000원
        );

        List<OrderItem> orderItems2 = Arrays.asList(
                new OrderItem(2L, 2, new BigDecimal("15000.00")) // 상품 101, 2개, 개당 15,000원
        );

        List<OrderItem> orderItems3 = Arrays.asList(
                new OrderItem(3L, 2, new BigDecimal("15000.00")) // 상품 101, 2개, 개당 15,000원
        );

        Order order1 = Order.createOrder(1L,orderItems);
        Order order2 = Order.createOrder(1L,orderItems2);
        Order order3 = Order.createOrder(1L,orderItems3);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        order1.updateStatus(OrderStatus.COMPLETED);
        order2.updateStatus(OrderStatus.COMPLETED);
        order3.updateStatus(OrderStatus.COMPLETED);

        orderService.updateStatus(order1);
        orderService.updateStatus(order2);
        orderService.updateStatus(order3);

        productFacade.getTopProductsBySales(5);
        productFacade.getTopProductsBySales(5);
        productFacade.getTopProductsBySales(5);

        verify(orderRepository, times(1)).findTopSellingProducts(5);

    }
}
