package kr.hhplus.be.server.interfaces.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.facade.CouponFacade;
import kr.hhplus.be.server.support.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@Tag(name = "CouponController", description = "쿠폰")
@RequiredArgsConstructor
public class CouponController {

    private final CouponFacade couponFacade;

    @GetMapping("/coupons/{userId}")
    @Operation(
            summary = "쿠폰 조회",
            description = "사용자의 쿠폰목록을 조회힌다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "쿠폰 목록 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CouponResponse.CouponRegisterV1.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public CustomApiResponse<List<CouponResponse.CouponRegisterV1>>getUserCoupons(@PathVariable(name="userId") long userId) {
        return CustomApiResponse.ok(couponFacade.getUserCoupons(userId), "쿠폰 목록 조회에 성공했습니다.");
    }

    @PostMapping("/coupons/issue")
    @Operation(
            summary = "선착순 쿠폰 발급",
            description = "쿠폰을 선착순으로 발급 한다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "선착순 쿠폰 발급",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponRequest.CouponRegisterV1.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CouponResponse.IssuedCouponRegisterV1.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public CustomApiResponse<CouponResponse.IssuedCouponRegisterV1> issueCoupon(@RequestBody CouponRequest.CouponRegisterV1 couponRequest) {
        return CustomApiResponse.ok(couponFacade.issueCoupon(couponRequest), "쿠폰 발급에 성공했습니다.");
    }
}
