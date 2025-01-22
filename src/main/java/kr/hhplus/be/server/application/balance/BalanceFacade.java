package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.application.balance.request.BalanceInfo;
import kr.hhplus.be.server.application.balance.response.BalanceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceFacade {

    private final BalanceService balanceService;
    private final UserService userService;

    @Transactional
    public BalanceResult.BalanceRegisterV1 getUserBalance(long userId) {
        User user = userService.getUserById(userId);
        UserBalance balance = balanceService.getByUserId(user.getId());
        return new BalanceResult.BalanceRegisterV1(user.getId(),  user.getUserName(), balance.getCurrentBalance());
    }
    @Transactional
    public BalanceResult.BalanceRegisterV2 charge(BalanceInfo.BalanceRegisterV1 balanceRequest) {
        User user = userService.getUserById(balanceRequest.userId());
        UserBalance balance = balanceService.updateBalance(user.getId(), balanceRequest.amount());
        return new BalanceResult.BalanceRegisterV2(user.getId(), balance.getCurrentBalance());
    }
}
