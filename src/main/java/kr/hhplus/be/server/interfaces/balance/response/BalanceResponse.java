package kr.hhplus.be.server.interfaces.balance.response;

import kr.hhplus.be.server.application.balance.response.BalanceResult;

import java.math.BigDecimal;

public class BalanceResponse {
    public record  UserBalanceResponse(long userId, String userName, BigDecimal currentBalance)
    {
        public static BalanceResponse.UserBalanceResponse of(BalanceResult.BalanceRegisterV1 response){
            return new BalanceResponse.UserBalanceResponse(response.userId(), response.userName(), response.currentBalance());

        }
    }
}
