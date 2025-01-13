package kr.hhplus.be.server.support;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiControllerAdvice  {

    @ExceptionHandler(HangHeaException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleHangHeaException(HangHeaException e) {
        CustomApiResponse<Void> response = CustomApiResponse.fail(e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }
}