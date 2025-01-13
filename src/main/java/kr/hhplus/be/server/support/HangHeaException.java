package kr.hhplus.be.server.support;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HangHeaException extends RuntimeException {
        private final ErrorCode errorCode;

        public HangHeaException(ErrorCode errorCode) {
            super(errorCode.getMessage());
            this.errorCode = errorCode;
        }

    public String getCode() {
            return errorCode.getCode();
        }

        public String getMessage() {
            return errorCode.getMessage();
        }

        public HttpStatus getHttpStatus() {
            return errorCode.getHttpStatus();
        }
}