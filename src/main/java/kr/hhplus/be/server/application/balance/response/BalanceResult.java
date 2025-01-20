package kr.hhplus.be.server.application.balance.response;


import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;


public class BalanceResult {
    @Schema(description = "잔액 조회 V1", example = """
        {
            "userId": 1,
            "userName": "임동욱",
            "currentBalance": 10000.00
        }
    """)
    public record BalanceRegisterV1(
            @Schema(description = "사용자 ID", example = "1")
                                        Long userId,

            @Schema(description = "사용자 이름", example = "임동욱")
                String userName,

            @Schema(description = "현재 잔액", example = "10000.00")
                BigDecimal currentBalance) {
    }

    @Schema(description = "잔액 충전 V2", example = """
        {
            "userId": 1,
            "currentBalance": 10000.00
        }
    """)
    public record BalanceRegisterV2(
            @Schema(description = "사용자 ID", example = "1")
            Long userId,

            @Schema(description = "현재 잔액", example = "10000.00")
            BigDecimal currentBalance) {
    }

}