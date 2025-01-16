package kr.hhplus.be.server.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.coupon.request.CouponInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestCouponIntegration {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("쿠폰발급요청시 정상적으로 발급된다.")
    @Transactional
    void issuedCoupon() throws Exception {
        // Given: JPA를 통해 데이터 삽입
        CouponInfo.CouponRegisterV1 couponRegisterV1 = new CouponInfo.CouponRegisterV1(1,1);
        String requestJson = objectMapper.writeValueAsString(couponRegisterV1);

        mockMvc.perform(post("/api/v1/coupons/issue")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(requestJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.data.issuedCouponId").value(couponRegisterV1.couponId()))
                .andExpect(jsonPath("$.data.userId").value(couponRegisterV1.userId()))
                .andExpect(header().string("Content-Type", "application/json")); // 응답 헤더 검증
    }

    @Test
    @DisplayName("사용자의 쿠폰 조회시 정상적으로 조회한다.")
    void shouldReturnUserIssuedCoupon() throws Exception {

       CouponInfo.CouponRegisterV1 couponRegisterV1 = new CouponInfo.CouponRegisterV1(1,1);
       String requestJson = objectMapper.writeValueAsString(couponRegisterV1);

       mockMvc.perform(post("/api/v1/coupons/issue")
                       .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                       .content(requestJson)) // JSON 문자열로 요청 데이터 전달
               .andExpect(status().isOk()); // HTTP 200 확인

        mockMvc.perform(get("/api/v1/coupons/{userId}",couponRegisterV1.userId())
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(requestJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[" + 0 + "].name").value("10% 할인 쿠폰" ))
                .andExpect(header().string("Content-Type", "application/json")); // 응답 헤더 검증
    }

}
