package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.order.ProductTopResult;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    @Transactional
    public Order createOrder(User user, List<Product> productItem, List<OrderRequest.OrderProductRegisterV1> products) {
        // 1. 주문 생성
        Order order = new Order();
        order.assignUser(user);
        order.updateStatus(OrderStatus.PENDING);

        // 2. 주문 항목 추가
        for (int i = 0; i < productItem.size(); i++) {
            Product product = productItem.get(i);
            OrderRequest.OrderProductRegisterV1 productDTO = products.get(i);

            // 주문 항목 생성
            OrderItem orderItem = new OrderItem(product, productDTO.quantity(), product.getPrice());
            order.addOrderItem(orderItem);  // 주문에 항목 추가
        }
        order.calculateTotalPrice();
        // 3. 주문 저장
        orderRepository.save(order);

        return order;

    }
    @Transactional
    public Order findOrderForUpdate(long orderId) {
        return orderRepository.findOrderForUpdate(orderId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.ORDER_NOT_FOUND));
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public List<ProductTopResult> findTopSellingProducts(int limit) {
        return orderRepository.findTopSellingProducts(limit);
    }
}
