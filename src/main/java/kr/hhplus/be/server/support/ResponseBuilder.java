package kr.hhplus.be.server.support;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public final class ResponseBuilder {

    private static final String DEFAULT_SUCCESS_MESSAGE = "요청이 성공적으로 처리되었습니다.";
    private static final String DEFAULT_FAIL_MESSAGE = "요청 처리에 실패했습니다.";
    // 성공/실패 메시지만 처리
    public static ResponseEntity<Map<String, Object>> build(Object data, HttpStatus status) {
        return build(data, status, status.is2xxSuccessful() ? DEFAULT_SUCCESS_MESSAGE : DEFAULT_FAIL_MESSAGE);
    }

    // 메시지만 처리
    public static ResponseEntity<Map<String, Object>> build(Object data, HttpStatus status, String message) {
        return build(data, status, message, "E000"); // 기본 실패 코드 "E000"
    }

    // 예외 코드까지 처리
    public static ResponseEntity<Map<String, Object>> build(Object data, HttpStatus status, String message, String errorCode) {
        Map<String, Object> response = createResponse(data, status, message, errorCode);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        return new ResponseEntity<>(response, headers, status);
    }

    private static Map<String, Object> createResponse(Object data, HttpStatus status, String message, String errorCode) {
        boolean isSuccess = status.is2xxSuccessful();

        return Map.of(
                "resultCode", isSuccess ? "S000" : errorCode,
                "status", isSuccess ? "SUCCESS" : "FAILURE",
                "data", isSuccess ? data : new HashMap<>(),
                "message", message
        );
    }
}
