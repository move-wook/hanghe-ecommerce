package kr.hhplus.be.server.config.filter;


import kr.hhplus.be.server.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterRegistration() {
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoggingFilter());
        registrationBean.addUrlPatterns("/api/*"); // /api/로 시작하는 경로에만 필터 적용
        registrationBean.setOrder(1); // 필터 실행 순서 (숫자가 낮을수록 먼저 실행)
        return registrationBean;
    }
}
