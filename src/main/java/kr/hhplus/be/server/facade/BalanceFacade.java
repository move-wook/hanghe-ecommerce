package kr.hhplus.be.server.facade;

import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.interfaces.balance.BalanceRequest;
import kr.hhplus.be.server.interfaces.balance.BalanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceFacade {

    private final BalanceService balanceService;
    private final UserService userService;

    public BalanceResponse.BalanceRegisterV1 getUserBalance(long userId) {
        User user = userService.getUserById(userId);
        UserBalance balance = balanceService.getByUserId(userId);

        return new BalanceResponse.BalanceRegisterV1(user.getId(),  user.getUserName(), balance.getCurrentBalance());
    }

    public BalanceResponse.BalanceRegisterV2 charge(BalanceRequest.BalanceRegisterV1 balanceRequest) {
        User user = userService.getUserById(balanceRequest.userId());
        UserBalance balance = balanceService.findBalanceForUpdate(user.getId());
        balanceService.updateBalance(balance, balanceRequest.amount());
        return new BalanceResponse.BalanceRegisterV2(user.getId(), balance.getCurrentBalance());
    }
}
