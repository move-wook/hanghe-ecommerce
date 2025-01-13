package kr.hhplus.be.server.infra.product;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.product.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    @Query("SELECT b FROM ProductInventory b WHERE b.product.id = :productId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ProductInventory> findProductInventoryForUpdate(@Param("productId")long productId);

}
