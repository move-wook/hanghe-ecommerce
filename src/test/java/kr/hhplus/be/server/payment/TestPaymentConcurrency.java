package kr.hhplus.be.server.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.balance.BalanceFacade;
import kr.hhplus.be.server.application.balance.response.BalanceResult;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.application.payment.request.PaymentInfo;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.balance.UserBalance;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInventory;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.balance.JpaUserBalanceRepository;
import kr.hhplus.be.server.infra.coupon.JpaCouponRepository;
import kr.hhplus.be.server.infra.coupon.JpaIssuedCouponRepository;
import kr.hhplus.be.server.infra.order.JpaOrderItemRepository;
import kr.hhplus.be.server.infra.order.JpaOrderRepository;
import kr.hhplus.be.server.infra.payment.JpaPaymentRepository;
import kr.hhplus.be.server.infra.product.JpaProductInventoryRepository;
import kr.hhplus.be.server.infra.product.JpaProductRepository;
import kr.hhplus.be.server.infra.user.JpaUserRepository;
import kr.hhplus.be.server.interfaces.balance.request.BalanceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class TestPaymentConcurrency {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JpaUserBalanceRepository userBalanceRepository;
    @Autowired
    private JpaOrderRepository orderRepository;
    @Autowired
    private JpaOrderItemRepository orderItemRepository;
    @Autowired
    private JpaProductRepository productRepository;
    @Autowired
    private JpaProductInventoryRepository productInventoryRepository;
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private JpaCouponRepository  couponRepository;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JpaCouponRepository jpaCouponRepository;
    @Autowired
    private JpaUserBalanceRepository jpaUserBalanceRepository;
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private JpaPaymentRepository jpaPaymentRepository;
    @Autowired
    private JpaIssuedCouponRepository jpaIssuedCouponRepository;
    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private BalanceFacade balanceFacade;
    @Autowired
    private ProductService productService;
    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Test
    @DisplayName("여러번 결재를 요청해도 한번만 결제가 되어야한다.")
    void concurrentProcessPaymentTest() throws  Exception{

        // 테스트 상품 생성
        Product product = Product.builder()
                .name("동시성제품")
                .price(BigDecimal.valueOf(19900))
                .description("테스트")
                .build();
        User user = User.builder().userName("임동욱").build();

        user = userRepository.save(user);

        product = productRepository.save(product);

        ProductInventory productInventory = ProductInventory.builder()
                .stock(20)
                .productId(product.getId())
                .build();

        productInventory = productInventoryRepository.save(productInventory);

        List<OrderItem> orderItemList = new ArrayList<>();
        // 주문 생성
        // 주문 항목 생성
        OrderItem orderItem = new OrderItem(product.getId(), 5, product.getPrice());
        orderItemList.add(orderItem);
        Order order = Order.createOrder(user.getId(), orderItemList);

        Order newOrder = orderRepository.save(order);

        orderItemList.forEach(item -> item.assignOrder(newOrder.getId()));

        orderItem = orderItemRepository.save(orderItem);

        Coupon coupon = Coupon.builder()
                .name("항해8기 수강 10%할인")
                .issuedCount(0)
                .limitCount(30)
                .validFrom(LocalDateTime.now())
                .validUntil(LocalDateTime.now().plusDays(10))
                .discountValue(BigDecimal.valueOf(10))
                .discountType("PERCENTAGE")
                .minimumOrderAmount(BigDecimal.valueOf(10))
                .build();
        coupon = jpaCouponRepository.save(coupon);

        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .userId(user.getId())
                .couponId(coupon.getId())
                .createdAt(LocalDateTime.now())
                .used(false)
                .status("PENDING")
                .build();
        issuedCoupon = jpaIssuedCouponRepository.save(issuedCoupon);

        UserBalance userBalance = UserBalance.builder()
                .currentBalance(BigDecimal.valueOf(100000))
                .userId(user.getId())
                .build();
        jpaUserBalanceRepository.save(userBalance);

        int tryCount = 5;

        ExecutorService executorService = Executors.newFixedThreadPool(tryCount);
        CountDownLatch latch = new CountDownLatch(tryCount);
        PaymentInfo.PaymentRegisterV1 paymentRequest = new PaymentInfo.PaymentRegisterV1(order.getId(),user.getId(),issuedCoupon.getId());
        for (int i = 0; i < tryCount; i++) {
            executorService.submit(() -> {
                try {
                    paymentFacade.processPayment(paymentRequest);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        BigDecimal discountRate = coupon.getDiscountValue();
        discountRate = discountRate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        BigDecimal discountFactor = BigDecimal.ONE.subtract(discountRate); // 1 - 0.10 = 0.90
        BigDecimal discountedTotal = total.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
        // 곱셈 수행
        BalanceRequest.BalanceInfo info = new BalanceRequest.BalanceInfo(user.getId());

        BalanceResult.BalanceRegisterV1 pointResponse = balanceFacade.getUserBalance(info);

        assertThat(userBalance.getCurrentBalance().subtract(discountedTotal)).isEqualTo(pointResponse.currentBalance());
        jpaProductRepository.delete(product);
        jpaPaymentRepository.deleteAll();
    }

    @Test
    @DisplayName("동시성 테스트: 재고는 음수로 떨어지지 않아야 한다.")
    void concurrentStockDecrementTest() throws Exception {


        // 테스트 상품 생성
        Product product = Product.builder()
                .name("동시성 재고 테스트 상품")
                .price(BigDecimal.valueOf(10000))
                .description("테스트 상품")
                .build();

        User user = User.builder().userName("임동욱").build();

        user = userRepository.save(user);

        product = productRepository.save(product);

        // 초기 재고 설정
        int initialStock = 20;
        ProductInventory productInventory = ProductInventory.builder()
                .stock(initialStock)
                .productId(product.getId())
                .build();

        productInventory = productInventoryRepository.save(productInventory);

        List<OrderItem> orderItemList = new ArrayList<>();
        // 주문 생성
        // 주문 항목 생성
        OrderItem orderItem = new OrderItem(product.getId(), 2, product.getPrice());
        orderItemList.add(orderItem);
        Order order = Order.createOrder(user.getId(), orderItemList);

        Order newOrder = orderRepository.save(order);

        orderItemList.forEach(item -> item.assignOrder(newOrder.getId()));

        orderItem = orderItemRepository.save(orderItem);


        // 동시성 테스트 설정
        int threadCount = 11; // 동시에 요청을 보낼 쓰레드 개수
        int decrementQuantity = 2; // 각 쓰레드가 차감하려는 재고 수량
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    // 재고 차감 로직 실행
                    productService.validateAndDeductStock(orderItemList);
                } catch (Exception e) {
                    System.err.println("에러 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        // 쓰레드 종료 대기
        latch.await();
        executorService.shutdown();

        // 결과 검증
        ProductInventory updatedInventory = productInventoryRepository.findById(productInventory.getId())
                .orElseThrow(() -> new RuntimeException("상품재고가 모두 품절되었습니다."));

        int expectedStock = Math.max(0, initialStock - (threadCount * decrementQuantity));

        assertThat(updatedInventory.getStock()).isEqualTo(expectedStock);


        jpaProductRepository.delete(product);
        jpaPaymentRepository.deleteAll();
    }

}
