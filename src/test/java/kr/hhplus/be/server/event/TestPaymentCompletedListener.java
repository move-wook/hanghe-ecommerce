package kr.hhplus.be.server.event;

import kr.hhplus.be.server.application.external.PaymentCompletedListener;
import kr.hhplus.be.server.external.EcommerceDataPlatform;
import kr.hhplus.be.server.external.PaymentCompletedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class TestPaymentCompletedListener {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @MockitoSpyBean
    private EcommerceDataPlatform ecommerceDataPlatform; // 실제 의존성을 Mock으로 대체

    @Test
    void 결제완료_이벤트가_주문상태변경_리스너에서_처리되는지_확인() {
        // given
        long orderId = 100L;
        long userId = 200L;
        long amount = 5000;
        long paymentId = 100L;
        PaymentCompletedEvent event = new PaymentCompletedEvent(orderId, userId, BigDecimal.valueOf(amount).longValue(),paymentId);

        // when
        eventPublisher.publishEvent(event);

        // then
        verify(ecommerceDataPlatform, times(1)).send(event); // send() 메서드가 한 번 호출되었는지 검증
    }
}
