package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.application.balance.request.BalanceInfo;
import kr.hhplus.be.server.application.balance.response.BalanceResult;
import kr.hhplus.be.server.interfaces.balance.request.BalanceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceFacade {

    private final BalanceService balanceService;
    private final UserService userService;

    public BalanceResult.BalanceRegisterV1 getUserBalance(BalanceRequest.BalanceInfo info) {
        BalanceInfo.BalanceUserV1 balanceUserV1 = BalanceInfo.BalanceUserV1.from(info);
        User user = userService.getUserById(balanceUserV1.userId());
        UserBalance balance = balanceService.getByUserId(user.getId());
        return new BalanceResult.BalanceRegisterV1(user.getId(),  user.getUserName(), balance.getCurrentBalance());
    }
    @Transactional
    public BalanceResult.BalanceRegisterV1 charge(BalanceRequest.BalanceCharge balanceRequest) {
        BalanceInfo.BalanceRegisterV1 balanceUserV1 = BalanceInfo.BalanceRegisterV1.from(balanceRequest);
        User user = userService.getUserById(balanceUserV1.userId());
        UserBalance balance = balanceService.updateBalance(user.getId(), balanceUserV1.amount());
        return new BalanceResult.BalanceRegisterV1(user.getId(), user.getUserName(), balance.getCurrentBalance());
    }
}
