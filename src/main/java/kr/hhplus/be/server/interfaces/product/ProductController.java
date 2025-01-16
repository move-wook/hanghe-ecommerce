package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.product.ProductFacade;
import kr.hhplus.be.server.application.product.response.ProductPageResult;
import kr.hhplus.be.server.application.product.response.ProductResult;
import kr.hhplus.be.server.support.CustomApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                                    schema = @Schema(implementation = ProductResult.ProductRegisterV1.class))),
                    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public CustomApiResponse<ProductResult.ProductRegisterV1> getProduct(@PathVariable(name="productId")long productId) {
        return CustomApiResponse.ok(productFacade.getProductById(productId), "상품 조회에 성공했습니다.");
    }

    @GetMapping("/products")
    @Operation(
            summary = "상품 목록 조회",
            description = "상품을 조회한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductPageResult.ProductPageRegisterV1.class))),
                    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public CustomApiResponse<ProductPageResult.ProductPageRegisterV1> getProducts(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "name,asc", name = "sort") String sort
    ) {
        return CustomApiResponse.ok(productFacade.listProducts(page, size, sort), "상품 목록 조회에 성공했습니다.");
    }

    @GetMapping("/products/top")
    @Operation(summary = "상위 상품 조회 요청", description = "상위 상품 조회 정보를 조회한다.")
    public CustomApiResponse<List<ProductResult.ProductTopRegisterV1> > getTopProducts(@RequestParam(name = "limit", defaultValue = "5") int limit) {
        return CustomApiResponse.ok(productFacade.getTopProductsBySales(limit),"상위상품 조회에 성공했습니다.");
    }
}
