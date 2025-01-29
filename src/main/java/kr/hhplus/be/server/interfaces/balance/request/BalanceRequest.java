package kr.hhplus.be.server.interfaces.balance.request;

public class BalanceRequest{
    public record BalanceInfo(long userId){
    }
    public record BalanceCharge(long userId, long amount){
    }
}
