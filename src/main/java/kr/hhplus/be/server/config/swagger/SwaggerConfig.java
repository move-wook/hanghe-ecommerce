package kr.hhplus.be.server.config.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo(){
        return new Info()
                .title("항해 이커머스 Swagger")
                .description(
                        """
                        ### API 기능 소개
                        - **쿠폰**
                            - 쿠폰 발급
                            - 보유 쿠폰 조회
                        - **잔액**
                            - 잔액 조회
                            - 잔액 충전
                        - **상품**
                            - 상품 조회
                            - 상품 목록 조회
                        - **주문**
                            - 주문 요청
                            - 주문 결제 요청
                        """
                )
                .version("1.0.0");

    }
}
