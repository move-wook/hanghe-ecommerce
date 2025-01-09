package kr.hhplus.be.server.support;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ErrorCode {
	// 사용자 관련
	NOT_FOUND_USER("E100", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	// 잔액 관련
	INSUFFICIENT_BALANCE("E200", "잔액이 부족합니다.", HttpStatus.BAD_REQUEST),
	BALANCE_RESOURCE_LOCKED("E201", "현재 잔액이 업데이트되고 있습니다. 잠시 후 다시 시도해주세요", HttpStatus.SERVICE_UNAVAILABLE),
	INVALID_BALANCE("E202", "유효하지 않은 잔액입니다.", HttpStatus.BAD_REQUEST),
	MAX_BALANCE("E203", "최대금액을 넘은 금액을 충전할 수 없습니다.", HttpStatus.BAD_REQUEST),
	// 상품 관련
	NOT_FOUND_PRODUCT("E300", "해당 상품이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	PRODUCT_EXPIRED("E301", "상품재고가 모두 품절되었습니다.", HttpStatus.NO_CONTENT),
	PRODUCT_INVENTORY_EXPIRED("E302", "상품에 대한 재고가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	// 쿠폰 관련
	INVALID_COUPON("E400", "유효하지 않은 쿠폰입니다.", HttpStatus.BAD_REQUEST),
	NOT_FOUND_USER_COUPON("E401", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	COUPON_EXPIRED("E402", "쿠폰재고가 모두 소진되었습니다.", HttpStatus.NO_CONTENT),
	COUPON_ALREADY_USED("E403", "이미 사용하신 쿠폰입니다.", HttpStatus.NO_CONTENT),

	// 주문 관련
	ORDER_NOT_FOUND("E500", "주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	// 결제 관련
	PAYMENT_FAILED("E600", "결제에 실패했습니다.", HttpStatus.PAYMENT_REQUIRED);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	ErrorCode(String code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}

}