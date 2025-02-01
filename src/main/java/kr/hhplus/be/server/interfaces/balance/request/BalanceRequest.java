package kr.hhplus.be.server.interfaces.balance.request;

public class BalanceRequest{
    public record BalanceInfo(long userId){
    }
    public record BalanceCharge(long userId, long amount){
        public static kr.hhplus.be.server.application.balance.request.BalanceInfo.BalanceRegisterV1 from(BalanceCharge info) {
            return new kr.hhplus.be.server.application.balance.request.BalanceInfo.BalanceRegisterV1(info.userId(), info.amount());
        }
    }
}
