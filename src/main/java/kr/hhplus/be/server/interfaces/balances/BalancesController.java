package kr.hhplus.be.server.interfaces.balances;

import kr.hhplus.be.server.support.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/v1")
@RestController
public class BalancesController {

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Map<String, Object>> getUserBalance(@PathVariable (name="userId") long userId) {
        return ResponseBuilder.build(Map.of(
                "resultCode", "S000",
                "status", "SUCCESS",
                "data", Map.of(
                        "userId", 123,
                        "currentBalance", 15000
                ),
                "message", "잔액 조회에 성공했습니다."
        ), HttpStatus.OK);
    }

    @PostMapping("/balance/charge")
    public ResponseEntity<Map<String, Object>> chargeBalance(@RequestBody BalanceRequest balanceRequest) {
        return ResponseBuilder.build(Map.of(
                "resultCode", "S000",
                "status", "SUCCESS",
                "message", "잔액 충전에 성공했습니다.",
                "data", Map.of(
                        "userId", 1,
                        "currentBalance", 20000
                )
        ), HttpStatus.OK);
    }
}
