package kr.hhplus.be.server.support;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
class ApiControllerAdvice  {

    @ExceptionHandler(HangHeaException.class)
    public ResponseEntity<Map<String, Object>> handleHangHeaException(HangHeaException e) {
        return ResponseBuilder.build(new HashMap<>(), e.getHttpStatus(), e.getMessage(), e.getCode());
    }
}