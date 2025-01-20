package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.application.order.request.OrderInfo;
import kr.hhplus.be.server.application.order.response.OrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    @Transactional
    public OrderResult.OrderRegisterV1 createOrder(OrderInfo.OrderRegisterV1 orderRequest) {
        //사용자 검증
        User user = userService.getUserById(orderRequest.userId());

        orderRequest.orderItems().forEach(item ->
                productService.validateStockAvailability(item.productId(), item.quantity())
        );

        List<Product> products = orderRequest.orderItems().stream()
                .map(item -> productService.getProductById(item.productId()))
                .toList();
        // 3. 유효성 검증 완료 후 도메인 서비스로 넘기기
        Order regisOrder =  orderService.processOrderCreation(user, products, orderRequest.orderItems());

        return new OrderResult.OrderRegisterV1(regisOrder.getId(), regisOrder.getTotalPrice().longValue(), regisOrder.getStatus().toString());
    }


}
