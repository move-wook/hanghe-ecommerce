package kr.hhplus.be.server.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EcommerceDataPlatform {
    public void send(PaymentCompletedEvent eventListener) {
        log.info("주문 이벤트 발생: 주문 ID = {}, {}, {}, {}",
                eventListener.paymentId(), eventListener.orderId(), eventListener.totalAmount(), eventListener.userId());
    }
}
