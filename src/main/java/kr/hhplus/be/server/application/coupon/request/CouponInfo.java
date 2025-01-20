package kr.hhplus.be.server.application.coupon.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class CouponInfo {
    @Schema(description = "쿠폰 조회 요청 데이터", example = """
        {
            "userId": 1,
            "couponId": 1
        }
    """)
    public record CouponRegisterV1(
            @Schema(description = "사용자 ID", example = "1")long userId,
            @Schema(description = "쿠폰 ID", example = "1")
            long couponId) {}
}
