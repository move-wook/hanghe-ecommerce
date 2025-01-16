package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.order.*;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.external.EcommerceDataPlatform;
import kr.hhplus.be.server.application.payment.request.PaymentInfo;
import kr.hhplus.be.server.application.payment.response.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentFacade {
    //사용자 서비스
    private final UserService userService;
    //주문서비스
    private final OrderService orderService;
    //상품서비스
    private final ProductService productService;
    //쿠폰서비스
    private final CouponService couponService;
    //잔액서비스
    private final BalanceService balanceService;
    //결제 테이블
    private final PaymentService paymentService;
    //데이터 플랫폼 전송
    private final EcommerceDataPlatform ecommerceDataPlatform;

    @Transactional
    public PaymentResult.PaymentRegisterV1 processPayment(PaymentInfo.PaymentRegisterV1 paymentRequest){
        //사용자 검증
        User user = userService.getUserById(paymentRequest.userId());
        //주문 서비스 조회 및 검증, 비관적 락
        Order order = orderService.validateOrderNotCompleted(paymentRequest.orderId());

        List<OrderItem> orderList = orderService.getByOrderIdWithOrderItem(order.getId());
        //상품의 재고 검증 및 차감
        productService.validateAndDeductStock(orderList);
        //총금액
        BigDecimal totalAmount = orderService.calculateTotalPrice(orderList);
        //쿠폰 할인 처리
        IssuedCoupon issuedCoupon = couponService.useCoupon(paymentRequest.issuedCouponId(), paymentRequest.userId());
        // 할인 금액 적용
        Coupon coupon = couponService.findByCouponId(issuedCoupon.getCouponId());

        BigDecimal discountMultiplier = BigDecimal.valueOf(100).subtract(coupon.getDiscountValue())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal discountAmount = totalAmount
                .multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);

        // 잔액 차감
        balanceService.deductBalance(user.getId(), discountAmount);
        // 6. 주문 상태 업데이트
        orderService.updateStatus(order);  // 주문 저장
        //결제 객체 생성
        Payment payment = new Payment(
                order.getId(),  // 주문 객체
                user.getId(),   // 사용자 객체
                discountAmount,  // 결제 금액
                "SUCCESS"  // 결제 상태
        );
        paymentService.createPayment(payment);

        ecommerceDataPlatform.send();

        return new PaymentResult.PaymentRegisterV1(order.getId(), discountAmount.longValue(), OrderStatus.COMPLETED.toString());
    }
}
