package kr.hhplus.be.server.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.coupon.request.CouponInfo;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.infra.coupon.JpaCouponRepository;
import kr.hhplus.be.server.infra.coupon.JpaIssuedCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TestCouponIntegration {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JpaCouponRepository jpaCouponRepository;
    @Autowired
    private JpaIssuedCouponRepository  jpaIssuedCouponRepository;

    @Test
    @DisplayName("쿠폰발급요청시 정상적으로 발급된다.")
    void issuedCoupon() throws Exception {
        jpaCouponRepository.deleteAll();
        jpaIssuedCouponRepository.deleteAll();

        Coupon coupon = Coupon.builder()
                .name("항해8기 수강 10%할인")
                .issuedCount(0)
                .limitCount(30)
                .validFrom(LocalDateTime.now())
                .validUntil(LocalDateTime.now().plusDays(10))
                .discountValue(BigDecimal.valueOf(10))
                .discountType("PERCENTAGE")
                .minimumOrderAmount(BigDecimal.valueOf(10))
                .build();
        Coupon mockCoupon = jpaCouponRepository.save(coupon);

        // Given: JPA를 통해 데이터 삽입
        CouponInfo.CouponRegisterV1 couponRegisterV1 = new CouponInfo.CouponRegisterV1(1, mockCoupon.getId());
        String requestJson = objectMapper.writeValueAsString(couponRegisterV1);

        mockMvc.perform(post("/api/v1/coupons/issue")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(requestJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.message").value("쿠폰 발급에 성공했습니다."))
                .andExpect(jsonPath("$.data.userId").value(couponRegisterV1.userId()))
                .andExpect(header().string("Content-Type", "application/json")); // 응답 헤더 검증

    }

    @Test
    @DisplayName("사용자의 쿠폰 조회시 정상적으로 조회한다.")
    void shouldReturnUserIssuedCoupon() throws Exception {
        Coupon coupon = Coupon.builder()
                .name("항해8기 수강 10%할인")
                .issuedCount(0)
                .limitCount(30)
                .validFrom(LocalDateTime.now())
                .validUntil(LocalDateTime.now().plusDays(10))
                .discountValue(BigDecimal.valueOf(10))
                .discountType("PERCENTAGE")
                .minimumOrderAmount(BigDecimal.valueOf(10))
                .build();
        Coupon mockCoupon = jpaCouponRepository.save(coupon);

       CouponInfo.CouponRegisterV1 couponRegisterV1 = new CouponInfo.CouponRegisterV1(1,mockCoupon.getId());
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
                .andExpect(jsonPath("$.data[" + 0 + "].name").value("항해8기 수강 10%할인" ))
                .andExpect(header().string("Content-Type", "application/json")); // 응답 헤더 검증

        jpaCouponRepository.deleteAll();
    }

}
