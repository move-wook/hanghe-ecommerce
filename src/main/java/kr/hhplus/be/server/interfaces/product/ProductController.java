package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.support.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1")
@RestController
public class ProductController {

    @GetMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable(name="productId")long productId) {
        return ResponseBuilder.build(Map.of(
                "resultCode", "S000",
                "status", "SUCCESS",
                "data", Map.of(
                "productId", 1,
                "name", "Product A",
                "price", 1000,
                "description", "상품 A 설명",
                "stock" , 3 //상품재고
                ),
                "message", "상품 조회에 성공했습니다."
        ), HttpStatus.OK);
    }

    @GetMapping("/products/top")
    public ResponseEntity<Map<String, Object>> getTopProducts() {
        List<Map<String, Object>> topProducts = List.of(
                Map.of("productId", 1, "name", "Product A", "totalSold", 500),
                Map.of("productId", 2, "name", "Product B", "totalSold", 300),
                Map.of("productId", 3, "name", "Product C", "totalSold", 200)
        );
        return ResponseBuilder.build(Map.of(
                "resultCode", "S000",
                "status", "SUCCESS",
                "data", topProducts,
                "message", "상위상품 조회에 성공했습니다."
        ), HttpStatus.OK);
    }
}
