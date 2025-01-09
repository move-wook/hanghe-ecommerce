package kr.hhplus.be.server.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.payment.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestPaymentService {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("정상적으로 결제정보를 저장한다.")
    void shouldSavePaymentSuccessfully() {
        // Given
        Payment payment = Payment.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(1000))
                .status("SUCCESS")
                .build();

        // Mocking save method (void 처리)
        doNothing().when(paymentRepository).save(payment);

        // When
        paymentService.save(payment);

        // Then
        verify(paymentRepository, times(1)).save(payment);
    }
}
