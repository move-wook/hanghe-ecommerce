package kr.hhplus.be.server.infra.balance;

import kr.hhplus.be.server.domain.balance.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserBalanceRepository extends JpaRepository<UserBalance, Long> {
    UserBalance findByUserId(long userId);
}
