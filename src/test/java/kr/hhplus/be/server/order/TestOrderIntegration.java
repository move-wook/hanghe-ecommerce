package kr.hhplus.be.server.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.order.request.OrderInfo;
import kr.hhplus.be.server.infra.order.JpaOrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestOrderIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaOrderRepository orderRepository;


    @Test
    @DisplayName("티셔츠 5장을 주문요청한다.")
    @Transactional
    void createOrder() throws Exception {

        // Given: JPA를 통해 데이터 삽입
        OrderInfo.OrderProductRegisterV1 orderProductRequest = new OrderInfo.OrderProductRegisterV1(1, 5);
        List<OrderInfo.OrderProductRegisterV1> orderList = new ArrayList<>();
        orderList.add(orderProductRequest);

        OrderInfo.OrderRegisterV1 orderRequest = new OrderInfo.OrderRegisterV1(1,orderList);
        String requestJson = objectMapper.writeValueAsString(orderRequest);
        BigDecimal price = new BigDecimal(19900);
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(requestJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.data.orderId").value(1))
                .andExpect(jsonPath("$.data.totalPrice").value(price.multiply(BigDecimal.valueOf(orderProductRequest.quantity()))))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(header().string("Content-Type", "application/json")); // 응답 헤더 검증
    }



}
