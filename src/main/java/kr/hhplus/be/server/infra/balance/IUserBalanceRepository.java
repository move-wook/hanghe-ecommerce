package kr.hhplus.be.server.infra.balance;

import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.domain.balance.UserBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IUserBalanceRepository implements UserBalanceRepository {
    private final JpaUserBalanceRepository JpaUserBalanceRepository;

    @Override
    public Optional<UserBalance> findByUserId(long userId) {
        return JpaUserBalanceRepository.findByUserId(userId);
    }
    @Override
    public UserBalance save(UserBalance balance) {
        return JpaUserBalanceRepository.save(balance);
    }
}
