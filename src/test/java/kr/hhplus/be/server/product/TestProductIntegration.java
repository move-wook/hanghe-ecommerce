package kr.hhplus.be.server.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestProductIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("단일 상품을 조회힌다..")
    void shouldReturnProduct() throws Exception {
        mockMvc.perform(get("/api/v1/product/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)) // 요청 타입 명시
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.data.name").value("티셔츠" ))
                .andExpect(jsonPath("$.data.id").value(1 ))
                .andExpect(jsonPath("$.data.price").value(19900 ))
                .andExpect(header().string("Content-Type", "application/json")); // 응답 헤더 검증
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void shouldReturnProductList() throws Exception {
        mockMvc.perform(get("/api/v1/products" )
                        .contentType(MediaType.APPLICATION_JSON)) // 요청 타입 명시
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.data.products").isArray())
                .andExpect(jsonPath("$.data.products.length()").value(4))
                .andExpect(header().string("Content-Type", "application/json")); // 응답 헤더 검증
    }
}
