package kr.hhplus.be.server.application.external;

import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.external.EcommerceDataPlatform;
import kr.hhplus.be.server.external.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCompletedListener {

    private final EcommerceDataPlatform ecommerceDataPlatform;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(OrderEvent event) {
        try{
            log.info("결제 완료 이벤트 수신 확인 - paymentId: {}, orderId: {}", event.paymentId(), event.orderId());
            ecommerceDataPlatform.send(event);
        } catch (Exception e){
            log.error("결제 데이터 처리 중 오류 발생", e);
        }
    }
}
