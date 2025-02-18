package kr.hhplus.be.server.application.external;

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
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        ecommerceDataPlatform.send(event);
    }
}
