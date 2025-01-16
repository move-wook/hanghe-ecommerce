package kr.hhplus.be.server.support;

import lombok.Getter;

@Getter
public class CustomApiResponse<T> {
    private final String resultCode;
    private final String status;
    private final String message;
    private final T data;

    public static <T> CustomApiResponse<T> ok(T data) {
        return new CustomApiResponse<>("S000", "SUCCESS", "요청이 성공적으로 처리되었습니다.", data);
    }

    public static <T> CustomApiResponse<T> ok(T data, String message) {
        return new CustomApiResponse<>("S000", "SUCCESS", message, data);
    }

    public static <T> CustomApiResponse<T> fail(String errorCode, String message) {
        return new CustomApiResponse<>(errorCode, "FAILURE", message, null);
    }

    private CustomApiResponse(String resultCode, String status, String message, T data) {
        this.resultCode = resultCode;
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
