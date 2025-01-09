package kr.hhplus.be.server.interfaces.balance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.facade.BalanceFacade;
import kr.hhplus.be.server.support.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Tag(name = "BalanceController", description = "잔액")
public class BalanceController {

    private final BalanceFacade balanceFacade;

    @Operation(
            summary = "잔액 조회",
            description = "특정 사용자의 잔액을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "잔액 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/balances/{userId}")
    @Parameter(name = "userId", description = "사용자 ID", example = "1")
    public ResponseEntity<Map<String, Object>> getUserBalance(@PathVariable (name="userId") long userId) {
        return ResponseBuilder.build(balanceFacade.getUserBalance(userId), HttpStatus.OK, "잔액 조회에 성공했습니다.");
    }

    @Operation(
            summary = "잔액 충전",
            description = "특정 사용자의 잔액을 충전합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "잔액 충전을 위한 요청 정보",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BalanceRequest.BalanceRegisterV1.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "잔액 충전 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/balance/charge")
    public ResponseEntity<Map<String, Object>> chargeBalance(@RequestBody BalanceRequest.BalanceRegisterV1 balanceRequest) {
        ;
        return ResponseBuilder.build(balanceFacade.charge(balanceRequest), HttpStatus.OK, "잔액 충전 성공");
    }
}
