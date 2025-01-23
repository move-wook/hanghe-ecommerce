package kr.hhplus.be.server.balance;

import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.domain.balance.UserBalanceRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestBalanceService {

    @Mock
    private UserBalanceRepository userBalanceRepository;
    @InjectMocks
    private BalanceService balanceService;

    @Test
    @DisplayName("특정 사용자의 ID로 현재 잔액을 조회한다.")
    void shouldFindUserBalanceById() {
        long userId = 1L;

        UserBalance userBalance = UserBalance.builder()
                .id(1L)
                .userId(userId)
                .version(0)
                .currentBalance(BigDecimal.valueOf(1000))
                .build();

        when(userBalanceRepository.findByUserId(userId)).thenReturn(userBalance);

        UserBalance result = balanceService.getByUserId(userId);

        assertThat(userId).isEqualTo(result.getUserId());
        assertThat(BigDecimal.valueOf(1000)).isEqualTo(result.getCurrentBalance());
    }

    @Test
    @DisplayName("특정 사용자의 ID로 현재 잔액을 조회한다.")
    void findBalanceForUpdate() {
        long userId = 1L;

        UserBalance userBalance = UserBalance.builder()
                .id(1L)
                .userId(userId)
                .currentBalance(BigDecimal.valueOf(1000))
                .build();

        when(userBalanceRepository.findByUserId(userId)).thenReturn(userBalance);

        UserBalance result = balanceService.getByUserId(userId);

        assertThat(userId).isEqualTo(result.getUserId());
        assertThat(BigDecimal.valueOf(1000)).isEqualTo(result.getCurrentBalance());
    }

    @Test
    @DisplayName("특정 사용자의 잔액을 충전 시킨다.")
    void updateBalance(){
        long userId = 1L;

        UserBalance userBalance = UserBalance.builder()
                .userId(userId)
                .version(0)
                .currentBalance(BigDecimal.valueOf(1000))
                .build();

        // Mockito로 findByUserId() 호출 시, userBalance를 반환하도록 설정
        when(userBalanceRepository.findByUserId(userId)).thenReturn(userBalance);

        // Mockito에서 save() 메서드를 호출할 때, 실제로는 userBalance 객체가 업데이트되도록 설정
        when(userBalanceRepository.save(any(UserBalance.class))).thenReturn(userBalance);

        balanceService.updateBalance(userId, 500L);

        assertThat(BigDecimal.valueOf(1500)).isEqualTo(userBalance.getCurrentBalance());

        verify(userBalanceRepository, times(1)).save(userBalance);
    }

    @Test
    @DisplayName("유효하지 않은 잔액을 충전하면 HangHeaException 예외를 발생시킨다.")
    void throwExceptionWhenAmountIsNegativeOrZero() {
        // Given
        long userId = 1L;

        UserBalance userBalance = UserBalance.builder()
                .id(1L)
                .userId(userId)
                .currentBalance(BigDecimal.valueOf(1000))
                .build();


        HangHeaException exception1 = assertThrows(HangHeaException.class, () -> {
            balanceService.updateBalance(userId, 0);
        });
        assertThat(ErrorCode.INVALID_BALANCE).isEqualTo(exception1.getErrorCode());


        HangHeaException exception2 = assertThrows(HangHeaException.class, () -> {
            balanceService.updateBalance(userId, -500);
        });
        assertThat(ErrorCode.INVALID_BALANCE).isEqualTo(exception2.getErrorCode());


        verify(userBalanceRepository, never()).save(userBalance);
    }

    @Test
    @DisplayName("현재 잔액에서 사용한 잔액을 차감한다.")
    void deleteBalance(){
        long userId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(1000);
        BigDecimal deductionAmount = BigDecimal.valueOf(500);

        UserBalance userBalance = UserBalance.builder()
                .id(1L)
                .userId(userId)
                .currentBalance(initialBalance)
                .build();

        when(userBalanceRepository.findByUserId(userId)).thenReturn(userBalance);
        //잔액 차감
        balanceService.deductBalance(userId, deductionAmount);

        // Then
        assertThat(BigDecimal.valueOf(500)).isEqualTo(userBalance.getCurrentBalance());

    }

    @Test
    @DisplayName("잔액 감소시 현재 잔액이 부족하면 HangHeaException 잔액 부족 예외를 발생시킨다.")
    void throwExceptionWhenInsufficientBalance() {
        // Given
        long userId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal deductionAmount = BigDecimal.valueOf(200);

        User user = User.builder()
                .id(userId)
                .userName("임동욱").build();
        UserBalance userBalance = UserBalance.builder()
                .id(1L)
                .userId(userId)
                .currentBalance(initialBalance)
                .build();

        when(userBalanceRepository.findByUserId(userId)).thenReturn(userBalance);

        // When & Then
        HangHeaException exception = assertThrows(HangHeaException.class, () -> {
            balanceService.deductBalance(userId, deductionAmount);
        });
        assertThat(ErrorCode.INSUFFICIENT_BALANCE).isEqualTo(exception.getErrorCode());

    }
}
