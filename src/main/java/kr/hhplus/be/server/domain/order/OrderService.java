package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.order.ProductTopResult;
import kr.hhplus.be.server.application.order.request.OrderInfo;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order processOrderCreation(User user, List<Product> productItem, List<OrderInfo.OrderProductRegisterV1> products) {
        // 1. 주문 생성
        List<OrderItem> orderList = new ArrayList<>();
        for (int i = 0; i < productItem.size(); i++) {
            Product product = productItem.get(i);
            OrderInfo.OrderProductRegisterV1 productDTO = products.get(i);
            orderList.add(new OrderItem(product.getId(), productDTO.quantity(), product.getPrice()));
        }
        Order order = Order.createOrder(user.getId(), orderList);
        // 3. 주문 저장
        orderRepository.save(order);
        //4.주문 상세
        orderList.forEach(item -> item.assignOrder(order.getId()));
        orderRepository.saveAll(orderList);

        return order;

    }

    public void updateStatus(Order order) {
        order.updateStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    public List<ProductTopResult> findTopSellingProducts(int limit) {
        return orderRepository.findTopSellingProducts(limit);
    }

    public BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Order validateOrderNotCompleted(long orderId) {
        Order order = orderRepository.findOrderForUpdate(orderId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.ORDER_NOT_FOUND));
        order.validateNotCompleted();
        return order;
    }

    public List<OrderItem>  getByOrderIdWithOrderItem(Long id) {
        return orderRepository.findByOrderId(id);
    }
}
