package kr.hhplus.be.server.support;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
class ApiControllerAdvice  {

    @ExceptionHandler(HangHeaException.class)
    public ResponseEntity<Map<String, Object>> handleHangHeaException(HangHeaException e) {
        return ResponseBuilder.build(new HashMap<>(), e.getHttpStatus(), e.getMessage(), e.getCode());
    }
}