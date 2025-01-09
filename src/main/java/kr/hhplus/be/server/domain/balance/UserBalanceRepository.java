package kr.hhplus.be.server.domain.balance;

import java.util.Optional;

public interface UserBalanceRepository {
    Optional<UserBalance> findByUserId(long userId);
    Optional<UserBalance> findBalanceForUpdate(long userId);

    UserBalance save(UserBalance balance);
}
