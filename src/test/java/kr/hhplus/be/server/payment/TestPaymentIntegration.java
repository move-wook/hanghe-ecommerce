package kr.hhplus.be.server.payment;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.balance.request.BalanceInfo;
import kr.hhplus.be.server.application.coupon.request.CouponInfo;
import kr.hhplus.be.server.application.coupon.response.CouponResult;
import kr.hhplus.be.server.application.order.request.OrderInfo;
import kr.hhplus.be.server.application.order.response.OrderResult;
import kr.hhplus.be.server.application.payment.request.PaymentInfo;
import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.infra.balance.JpaUserBalanceRepository;
import kr.hhplus.be.server.infra.coupon.JpaCouponRepository;
import kr.hhplus.be.server.infra.payment.JpaPaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestPaymentIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaCouponRepository jpaCouponRepository;

    @Autowired
    private JpaUserBalanceRepository jpaUserBalanceRepository;

    @Autowired
    private JpaPaymentRepository jpaPaymentRepository;

    @Test
    @DisplayName("10만원을 가진 사용자가 주문을 요청해 결제를 할 때 10%할인 쿠폰을 적용해서 결재 후 외부데이터통신을 요청한다.")
    @Transactional
    void processPayment() throws Exception {
        jpaPaymentRepository.deleteAll();

        UserBalance userBalance = UserBalance.builder()
                .userId(1L)
                .version(0)
                .currentBalance(BigDecimal.valueOf(0L))
                .build();
        jpaUserBalanceRepository.save(userBalance);
        //사용자 잔액 충전
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

        BalanceInfo.BalanceRegisterV1 registerV1 = new BalanceInfo.BalanceRegisterV1(1L, 100000);
        String balanceJson = objectMapper.writeValueAsString(registerV1);
        mockMvc.perform(post("/api/v1/balance/charge")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(balanceJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk());// HTTP 200 확인
        //사용자 쿠폰 발급
        CouponInfo.CouponRegisterV1 couponRegisterV1 = new CouponInfo.CouponRegisterV1(1, mockCoupon.getId());
        String couponJson = objectMapper.writeValueAsString(couponRegisterV1);

        ResultActions issueResult = mockMvc.perform(post("/api/v1/coupons/issue")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(couponJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk()); // HTTP 200 확인

        String responseContent = issueResult.andReturn().getResponse().getContentAsString();

        // ObjectMapper를 사용해 응답을 Java 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // JSON을 JsonNode로 파싱
        JsonNode rootNode = objectMapper.readTree(responseContent);

        // "data" 부분만 추출
        JsonNode dataNode = rootNode.path("data");

        // dataNode를 CouponData 객체로 변환
        CouponResult.IssuedCouponRegisterV1 issuedCouponRegisterV1 = objectMapper.treeToValue(dataNode, CouponResult.IssuedCouponRegisterV1.class);

        //티셔츠 5장 주문요청
        OrderInfo.OrderProductRegisterV1 orderProductRequest = new OrderInfo.OrderProductRegisterV1(1, 5);
        List<OrderInfo.OrderProductRegisterV1> orderList = new ArrayList<>();
        orderList.add(orderProductRequest);

        OrderInfo.OrderRegisterV1 orderRequest = new OrderInfo.OrderRegisterV1(1, orderList);
        String requestJson = objectMapper.writeValueAsString(orderRequest);
        ResultActions resultOrder = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(requestJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk()); // HTTP 200 확인

        String orderResponse = resultOrder.andReturn().getResponse().getContentAsString();

        // ObjectMapper를 사용해 응답을 Java 객체로 변환
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // JSON을 JsonNode로 파싱
        rootNode = objectMapper.readTree(orderResponse);
        // "data" 부분만 추출
        dataNode = rootNode.path("data");

        // dataNode를 CouponData 객체로 변환
        OrderResult.OrderRegisterV1 orderRegisterV1 = objectMapper.treeToValue(dataNode,    OrderResult.OrderRegisterV1.class);

        PaymentInfo.PaymentRegisterV1 paymentRegisterV1 = new PaymentInfo.PaymentRegisterV1(orderRegisterV1.orderId(), 1, issuedCouponRegisterV1.issuedCouponId());
        String paymentJson = objectMapper.writeValueAsString(paymentRegisterV1);
        BigDecimal price = new BigDecimal(19900);

        BigDecimal discountMultiplier = BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(10))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal discountedTotal = price
                .multiply(BigDecimal.valueOf(orderProductRequest.quantity())) // 총 금액
                .multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(paymentJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.message").value("결제에 성공했습니다."))
                .andExpect(jsonPath("$.data.amount").value(discountedTotal.longValue()))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andExpect(header().string("Content-Type", "application/json")); // 응답 헤더 검증

    }
}
