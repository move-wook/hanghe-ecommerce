package kr.hhplus.be.server.infra.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.order.*;
import kr.hhplus.be.server.domain.product.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IOrderRepository implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final JpaOrderItemRepository jpaOrderItemRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public void save(Order order) {
        jpaOrderRepository.save(order);
    }

    @Override
    public Optional<Order> findOrderForUpdate(long orderId) {
        return jpaOrderRepository.findOrderForUpdate(orderId);
    }

    @Override
    public List<ProductTopResult> findTopSellingProducts(int limit) {
        QOrderItem orderItem = QOrderItem.orderItem;
        QProduct product = QProduct.product;
        QOrder order = QOrder.order; // 주문 날짜 필드가 포함된 엔티티

        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        return queryFactory.select(Projections.constructor(
                        ProductTopResult.class,
                        product.id,
                        product.name,
                        orderItem.quantity.sum().as("totalSold")
                ))
                .from(orderItem)
                .join(product).on(orderItem.productId.eq(product.id)) // FK로 조인
                .join(order).on(orderItem.orderId.eq(order.id)) // FK로 조인
                .where(order.createdAt.after(threeDaysAgo).and(order.status.eq(OrderStatus.valueOf("COMPLETED")))) // 최근 3일 조건 추가
                .groupBy(product.id, product.name)
                .orderBy(orderItem.quantity.sum().desc())
                .limit(limit) // 상위 5개 제한
                .fetch();
    }

    @Override
    public List<OrderItem> findByOrderId(Long id) {
        return jpaOrderItemRepository.findByOrderId(id);
    }

    @Override
    public void saveAll(List<OrderItem> orderList) {
        jpaOrderItemRepository.saveAll(orderList);
    }

}
