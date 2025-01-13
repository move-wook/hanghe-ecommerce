package kr.hhplus.be.server.infra.order;

import kr.hhplus.be.server.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT b FROM Order b WHERE b.id = :orderId")
    Optional<Order> findOrderForUpdate(@Param("orderId")long orderId);
}
