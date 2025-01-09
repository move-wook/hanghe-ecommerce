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
        // 금액 유효성 검증 (예: 음수 금액 방지)
        if (amount <= 0) {
            throw new HangHeaException(ErrorCode.INVALID_BALANCE);
        }
        // 기존 잔액과 요청 금액 합산
        BigDecimal newBalance = balance.getCurrentBalance().add(BigDecimal.valueOf(amount));

        // 충전 한도 초과 확인 (옵션)
        long maxBalance = 1_000_000L; // 예: 최대 잔액 1,000,000
        if (newBalance.longValue() > maxBalance) {
            throw new HangHeaException(ErrorCode.MAX_BALANCE);
        }

        // 잔액 업데이트
        balance.addBalance(newBalance);

        // 변경된 데이터 저장
        userBalanceRepository.save(balance);
    }

    @Transactional
    public void deductBalance(long userId, BigDecimal amount) {
        UserBalance userBalance = this.getByUserId(userId);
        // 잔액 부족 여부 확인
        if (userBalance.getCurrentBalance().compareTo(amount) < 0) {
            throw new HangHeaException(ErrorCode.INSUFFICIENT_BALANCE);  // 잔액 부족 예외 처리
        }
        // 잔액 차감
        BigDecimal newBalance = userBalance.getCurrentBalance().subtract(amount);
        userBalance.addBalance(newBalance);  // 잔액 업데이트
        // 잔액 변경 내용 저장
        userBalanceRepository.save(userBalance);
    }
}
