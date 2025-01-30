package kr.hhplus.be.server.domain.balance;

import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final UserBalanceRepository userBalanceRepository;

    public UserBalance getByUserId(long userId) {
        return userBalanceRepository.findByUserId(userId);
    }

    public UserBalance updateBalance(long userId, long amount) {
        UserBalance userBalance = userBalanceRepository.findByUserId(userId);
        // 잔액 추가
        userBalance.addBalance(BigDecimal.valueOf(amount));
        try {
            userBalanceRepository.save(userBalance);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new HangHeaException(ErrorCode.BALANCE_RESOURCE_LOCKED);
        }
        // 변경된 데이터 저장
        return userBalance;
    }

    @Transactional
    public void deductBalance(long userId, BigDecimal amount) {
        UserBalance userBalance = userBalanceRepository.findByUserId(userId);
        // 잔액 차감
        userBalance.subBalance(amount);
        // 잔액 변경 내용 저장
        userBalanceRepository.save(userBalance);
    }

}
