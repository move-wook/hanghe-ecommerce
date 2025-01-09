package kr.hhplus.be.server.facade;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.interfaces.order.OrderResponse;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    public OrderResponse.OrderRegisterV1 createOrder(OrderRequest.OrderRegisterV1 orderRequest) {
        //사용자 검증
        User user = userService.getUserById(orderRequest.userId());
        //상품 유효성 검사 및 재고 검증
        List<Product> products = new ArrayList<>();
        for (OrderRequest.OrderProductRegisterV1 productDTO : orderRequest.orderItems()) {
            Product product = productService.getProductById(productDTO.productId());
            if (product == null) {
                throw new HangHeaException(ErrorCode.NOT_FOUND_PRODUCT);  // 제품이 없는 경우 예외
            }
            long stock = product.getProductInventory().getStock();
            if (stock < productDTO.quantity()) {
                throw new HangHeaException(ErrorCode.PRODUCT_EXPIRED);  // 재고 부족 예외
            }
            products.add(product);  // 유효한 제품만 리스트에 추가
        }

        // 3. 유효성 검증 완료 후 도메인 서비스로 넘기기
        Order regisOrder =  orderService.createOrder(user, products, orderRequest.orderItems());

        return new OrderResponse.OrderRegisterV1(regisOrder.getId(), regisOrder.getTotalPrice().longValue(), regisOrder.getStatus().toString());
    }
}
