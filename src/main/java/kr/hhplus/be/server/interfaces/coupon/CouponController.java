package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.support.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1")
@RestController
public class CouponController {

    @GetMapping("/coupons/{userId}")
    public ResponseEntity<Map<String, Object>> getUserCoupons(@PathVariable(name="userId") long userId) {
        List<Map<String, Object>> userCoupons = List.of(
                Map.of("couponId", 101, "name", "10% 할인 쿠폰", "status", "AVAILABLE", "validUntil", "2025-12-31"),
                Map.of("couponId", 102, "name", "20% 할인 쿠폰", "status", "USED", "validUntil", "2025-10-15")
        );
        return ResponseBuilder.build(Map.of(
                "resultCode", "S000",
                "status", "SUCCESS",
                "data", userCoupons,
                "message", "쿠폰 조회에 성공했습니다."
        ), HttpStatus.OK);
    }

    @PostMapping("/coupons/issue")
    public ResponseEntity<Map<String, Object>> issueCoupon(@RequestBody CouponRequest couponRequest) {
        return ResponseBuilder.build(Map.of(
                "resultCode", "S000",
                "status", "SUCCESS",
                "data", Map.of(
                "userId", 123,
                "couponId", 101
                ),
                "message", "쿠폰 발급에 성공했습니다."
        ), HttpStatus.OK);
    }
}
