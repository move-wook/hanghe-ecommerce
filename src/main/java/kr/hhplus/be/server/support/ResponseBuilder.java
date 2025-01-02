package kr.hhplus.be.server.support;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public final class ResponseBuilder {

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String CHARSET_UTF8 = ";charset=UTF-8";

    private ResponseBuilder() {
        throw new UnsupportedOperationException();
    }

    public static ResponseEntity<Map<String, Object>> build(Map<String, Object> resMap, HttpStatus httpStatus) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + CHARSET_UTF8);
        Map<String, Object> httpBody = new HashMap<String, Object>();
        httpBody.put("resultCode", resMap.get("resultCode"));
        httpBody.put("status", resMap.get("status"));
        httpBody.put("data", resMap.get("data"));
        httpBody.put("message", resMap.get("message"));
        return new ResponseEntity<Map<String, Object>>(httpBody, httpHeaders, httpStatus);
    }
}
