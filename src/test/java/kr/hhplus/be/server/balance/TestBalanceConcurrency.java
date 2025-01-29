package kr.hhplus.be.server.balance;

import kr.hhplus.be.server.application.balance.BalanceFacade;
import kr.hhplus.be.server.application.balance.request.BalanceInfo;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.balance.JpaUserBalanceRepository;
import kr.hhplus.be.server.infra.user.JpaUserRepository;
import kr.hhplus.be.server.interfaces.balance.request.BalanceRequest;
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

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
public class TestBalanceConcurrency {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BalanceFacade balanceFacade;

    @Autowired
    private JpaUserBalanceRepository jpaUserBalanceRepository;
    @Autowired
    private JpaUserRepository jpaUserRepository;

    private long mockUserId = 1;

    @Test
    @DisplayName("chargeUserPoint - 동시성 문제 해결 테스트")
    public void 사용자가500원을_가지고있는경우_100원_충전을_여러번_요청하면_1번만_성공해야한다() throws InterruptedException {

        jpaUserBalanceRepository.deleteAll();
        UserBalance userBalance = UserBalance.builder()
                .userId(1L)
                .version(0)
                .currentBalance(BigDecimal.valueOf(0L))
                .build();
        jpaUserBalanceRepository.save(userBalance);

        long chargeAmount = 100L;
        int threadCount = 5;

        BalanceRequest.BalanceCharge registerV1 = new BalanceRequest.BalanceCharge(mockUserId, chargeAmount);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    balanceFacade.charge(registerV1);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        UserBalance currentUserBalance = balanceService.getByUserId(mockUserId);

        System.out.println("최종 잔액: " + currentUserBalance.getCurrentBalance().longValue() + ", 예상 잔액: " + 100);
        assertThat(100).isEqualTo(currentUserBalance.getCurrentBalance().longValue());
        jpaUserBalanceRepository.deleteAll();
    }




}
