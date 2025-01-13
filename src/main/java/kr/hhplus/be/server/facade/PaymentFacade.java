package kr.hhplus.be.server.facade;

import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInventory;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.external.EcommerceDataPlatform;
import kr.hhplus.be.server.interfaces.payment.PaymentRequest;
import kr.hhplus.be.server.interfaces.payment.PaymentResponse;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
    public PaymentResponse.PaymentRegisterV1 processPayment(PaymentRequest.PaymentRegisterV1 paymentRequest) {
        //사용자 검증
        User user = userService.getUserById(paymentRequest.userId());
        //주문 서비스 조회 및 검증, 비관적 락
        Order order = orderService.findOrderForUpdate(paymentRequest.orderId());
        //상품 검증 (재고가 품절인지)
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(OrderItem orderItem : order.getOrderItems()) {
            ProductInventory productInventory = productService.getProductInventoryForUpdate(orderItem.getProduct().getId());
            //주문할 상품 재고 보다 상품 재고가 작을 경우 품절 예외
            if(productInventory.getStock() < orderItem.getQuantity()) {
                throw new HangHeaException(ErrorCode.PRODUCT_EXPIRED);
            }
            productService.decrementStock(productInventory.getProduct().getId(), orderItem.getQuantity());
            totalAmount = totalAmount.add(orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }

        BigDecimal discountAmount = BigDecimal.ZERO;

        // 쿠폰 검증
        IssuedCoupon issuedCoupon = couponService.findByIdAndUserId(paymentRequest.issuedCouponId(), paymentRequest.userId());
        if (issuedCoupon.isUsed()) {
            throw new HangHeaException(ErrorCode.COUPON_ALREADY_USED);  // 유효하지 않은 쿠폰
        }

        // 할인 금액 적용
        discountAmount = issuedCoupon.getCoupon().getDiscountValue();
        totalAmount = totalAmount.subtract(discountAmount);  // 할인 금액 차감

        // 쿠폰 상태 업데이트 (사용됨)
        couponService.useCoupon(issuedCoupon.getId(), user.getId());

        UserBalance userBalance = balanceService.getByUserId(user.getId());
        // 잔액 차감
        balanceService.deductBalance(userBalance.getUser().getId(), totalAmount);

        // 6. 주문 상태 업데이트
        order.updateStatus(OrderStatus.COMPLETED);  // 주문 상태를 PAID로 설정
        orderService.save(order);  // 주문 저장
        // 결제 성공 응답 생성
        //결제 객체 생성
        Payment payment = new Payment(
                order,  // 주문 객체
                user,   // 사용자 객체
                totalAmount,  // 결제 금액
                "SUCCESS"  // 결제 상태
        );
        paymentService.save(payment);
        ecommerceDataPlatform.send();
        return new PaymentResponse.PaymentRegisterV1(order.getId(), totalAmount.longValue(), discountAmount.longValue(), OrderStatus.COMPLETED.toString());
    }
}
