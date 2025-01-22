package kr.hhplus.be.server.balance;

import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.infra.balance.JpaUserBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class TestBalanceConcurrency {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private JpaUserBalanceRepository jpaUserBalanceRepository;


    @BeforeEach
    void setup() {
        jpaUserBalanceRepository.deleteAll();
    }

    @Test
    @DisplayName("chargeUserPoint - 동시성 문제 해결 테스트")
    public void 사용자가100원을_가지고있는경우_500원_충전을_여러번_요청하면_1번만_성공해야한다() throws InterruptedException {
        long userId = 1L;
        long initialPoint = 500L;
        long chargeAmount = 100L;
        int threadCount = 5;

        UserBalance userBalance = UserBalance.builder()
                .userId(userId)
                .version(0)
                .currentBalance(BigDecimal.valueOf(500L))
                .build();

        jpaUserBalanceRepository.save(userBalance);



        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    balanceService.updateBalance(userId, chargeAmount);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long expectedTotal = initialPoint + chargeAmount;
        UserBalance currentUserBalance = balanceService.getByUserId(userId);

        System.out.println("최종 잔액: " + currentUserBalance.getCurrentBalance().longValue() + ", 예상 잔액: " + expectedTotal);
        assertEquals(expectedTotal, currentUserBalance.getCurrentBalance().longValue(), "동시성 문제로 인해 최종 포인트 값이 예상과 다릅니다.");
    }




}
