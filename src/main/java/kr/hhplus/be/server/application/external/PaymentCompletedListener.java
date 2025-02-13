package kr.hhplus.be.server.application.external;

import kr.hhplus.be.server.external.EcommerceDataPlatform;
import kr.hhplus.be.server.external.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCompletedListener {

    private final EcommerceDataPlatform ecommerceDataPlatform;

    @EventListener
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        ecommerceDataPlatform.send(event);
    }
}
