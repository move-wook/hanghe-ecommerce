package kr.hhplus.be.server.domain.balance;

import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final UserBalanceRepository userBalanceRepository;

    public UserBalance getByUserId(long userId) {
        return userBalanceRepository.findByUserId(userId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.NOT_FOUND_USER));

    }
    @Transactional
    public UserBalance findBalanceForUpdate(long userId) {
        return userBalanceRepository.findBalanceForUpdate(userId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.BALANCE_RESOURCE_LOCKED));

    }
    @Transactional
    public void updateBalance(UserBalance balance, long amount) {
        // 잔액 추가
        balance.addBalance(BigDecimal.valueOf(amount));
        // 변경된 데이터 저장
        userBalanceRepository.save(balance);
    }

    @Transactional
    public void deductBalance(long userId, BigDecimal amount) {
        UserBalance userBalance = this.getByUserId(userId);
        // 잔액 차감
        userBalance.subBalance(amount);
        // 잔액 변경 내용 저장
        userBalanceRepository.save(userBalance);
    }
}
