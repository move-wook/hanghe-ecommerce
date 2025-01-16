package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.infra.order.ProductTopResult;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);

    Optional<Order> findOrderForUpdate(long orderId);

    List<ProductTopResult> findTopSellingProducts(int limit);

    List<OrderItem>  findByOrderId(Long id);

    void saveAll(List<OrderItem> orderList);
}
