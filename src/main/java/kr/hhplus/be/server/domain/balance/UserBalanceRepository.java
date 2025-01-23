package kr.hhplus.be.server.domain.balance;

public interface UserBalanceRepository {
    UserBalance findByUserId(long userId);

    UserBalance save(UserBalance balance);
}