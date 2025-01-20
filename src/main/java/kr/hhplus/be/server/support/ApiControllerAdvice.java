package kr.hhplus.be.server.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiControllerAdvice  {

    private static final Logger log = LoggerFactory.getLogger(ApiControllerAdvice.class);

    @ExceptionHandler(HangHeaException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleHangHeaException(HangHeaException e) {

        log.error("Handled HangHeaException - Code: {}, Message: {}, HTTP Status: {}",
                e.getCode(), e.getMessage(), e.getHttpStatus(), e);

        CustomApiResponse<Void> response = CustomApiResponse.fail(e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }
}