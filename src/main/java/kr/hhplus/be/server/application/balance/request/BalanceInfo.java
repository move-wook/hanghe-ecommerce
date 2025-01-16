package kr.hhplus.be.server.application.balance.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class BalanceInfo {
    @Schema(description = "잔액 충전 요청 데이터", example = """
        {
            "userId": 1,
            "amount": 5000
        }
    """)
    public record BalanceRegisterV1(
            @Schema(description = "사용자 ID", example = "1")
                         long userId,

            @Schema(description = "충전할 금액", example = "5000")
                         long amount){
    }
}
