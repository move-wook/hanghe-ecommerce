package kr.hhplus.be.server.infra.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.QOrderItem;
import kr.hhplus.be.server.domain.product.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IOrderRepository implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
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
            return queryFactory.select(Projections.constructor(
                            ProductTopResult.class,
                            product.id,
                        product.name,
                        orderItem.quantity.sum().as("totalSold")
                ))
                .from(orderItem)
                .join(orderItem.product, product)
                .groupBy(product.id, product.name)
                .orderBy(orderItem.quantity.sum().desc())
                .limit(limit)
                .fetch();
    }

}
