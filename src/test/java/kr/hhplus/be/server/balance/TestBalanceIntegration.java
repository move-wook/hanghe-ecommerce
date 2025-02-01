package kr.hhplus.be.server.balance;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.balance.request.BalanceInfo;
import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.domain.balance.UserBalanceRepository;
import kr.hhplus.be.server.infra.balance.JpaUserBalanceRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestBalanceIntegration {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JpaUserBalanceRepository jpaUserBalanceRepository;

    @Test
    @DisplayName("잔액 충전 요청시 잔액이 정상 충전된다.")
    @Transactional
    void charge() throws Exception {
        // Given: JPA를 통해 데이터 삽입
        jpaUserBalanceRepository.deleteAll();
        UserBalance userBalance = UserBalance.builder()
                .userId(1L)
                .version(0)
                .currentBalance(BigDecimal.valueOf(0L))
                .build();
        jpaUserBalanceRepository.save(userBalance);

        BalanceInfo.BalanceRegisterV1 registerV1 = new BalanceInfo.BalanceRegisterV1(1L, 500);
        String requestJson = objectMapper.writeValueAsString(registerV1);

        mockMvc.perform(post("/api/v1/balance/charge")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(requestJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.data.currentBalance").value(500))
                .andExpect(header().string("Content-Type", "application/json")); // 응답 헤더 검증

        jpaUserBalanceRepository.deleteAll();
    }

    @Test
    @DisplayName("잔액 조회 요청시 정상적으로 조회된다..")
    void shouldReturnUserBalance() throws Exception {
        // Given: JPA를 통해 데이터 삽입
        jpaUserBalanceRepository.deleteAll();
        UserBalance userBalance = UserBalance.builder()
                .userId(1L)
                .version(0)
                .currentBalance(BigDecimal.valueOf(0L))
                .build();
        jpaUserBalanceRepository.save(userBalance);

        BalanceInfo.BalanceRegisterV1 registerV1 = new BalanceInfo.BalanceRegisterV1(1L, 500);
        String requestJson = objectMapper.writeValueAsString(registerV1);

        mockMvc.perform(post("/api/v1/balance/charge")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(requestJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/balances/{userId}",registerV1.userId())
                        .contentType(MediaType.APPLICATION_JSON) // 요청 타입 명시
                        .content(requestJson)) // JSON 문자열로 요청 데이터 전달
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.data.userId").value(registerV1.userId()))
                .andExpect(jsonPath("$.data.currentBalance").value(500))
                .andExpect(header().string("Content-Type", "application/json")); // 응답 헤더 검증

        jpaUserBalanceRepository.deleteAll();
    }



}
