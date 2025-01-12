package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.facade.ProductFacade;
import kr.hhplus.be.server.support.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1")
@RestController
@Tag(name = "ProductController", description = "상품")
@RequiredArgsConstructor
public class ProductController {

    private final ProductFacade productFacade;

    @GetMapping("/product/{productId}")
    @Operation(
            summary = "상품 조회",
            description = "상품을 조회한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable(name="productId")long productId) {
        return ResponseBuilder.build(productFacade.getProductById(productId), HttpStatus.OK, "상품 조회에 성공했습니다.");
    }

    @GetMapping("/products")
    @Operation(
            summary = "상품 목록 조회",
            description = "상품을 조회한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "name,asc", name = "sort") String sort
    ) {
        return ResponseBuilder.build(productFacade.listProducts(page, size, sort), HttpStatus.OK, "상품 목록 조회에 성공했습니다.");
    }

    @GetMapping("/products/top")
    @Operation(summary = "상위 상품 조회 요청", description = "상위 상품 조회 정보를 조회한다.")
    public ResponseEntity<Map<String, Object>> getTopProducts(@RequestParam(name = "limit", defaultValue = "5") int limit) {
        return ResponseBuilder.build(productFacade.getTopProductsBySales(limit),HttpStatus.OK,"상위상품 조회에 성공했습니다.");
    }
}
