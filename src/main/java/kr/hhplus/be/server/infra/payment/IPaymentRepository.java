package kr.hhplus.be.server.infra.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IPaymentRepository implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;

    @Override
    public void save(Payment payment) {
        jpaPaymentRepository.save(payment);
    }
}
