package kr.hhplus.be.server.infra.balance;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.balance.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaUserBalanceRepository extends JpaRepository<UserBalance, Long> {
    @Query("SELECT b FROM UserBalance b WHERE b.userId = :userId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserBalance> findBalanceForUpdate(@Param("userId")long userId);

    Optional<UserBalance> findByUserId(long userId);
}
