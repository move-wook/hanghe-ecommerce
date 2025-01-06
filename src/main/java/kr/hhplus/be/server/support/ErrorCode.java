package kr.hhplus.be.server.support;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ErrorCode {
	// 사용자 관련
	NOT_FOUND_USER("E100", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	// 잔액 관련
	INSUFFICIENT_BALANCE("E200", "잔액이 부족합니다.", HttpStatus.BAD_REQUEST),
	// 상품 관련
	NOT_FOUND_PRODUCT("E300", "해당 상품이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
	// 쿠폰 관련
	INVALID_COUPON("E400", "유효하지 않은 쿠폰입니다.", HttpStatus.BAD_REQUEST),
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