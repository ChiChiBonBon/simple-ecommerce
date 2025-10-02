package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.constant.ApiCode;
import com.changbenny.simpleecommerce.constant.ProductCategory;
import com.changbenny.simpleecommerce.dto.*;
import com.changbenny.simpleecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理 (Product)", description = "商品的查詢、新增、修改、刪除相關 API")
@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    @Operation(summary = "查詢商品列表", description = "支援分頁、排序、篩選條件查詢商品")
    public ResponseEntity<ApiResponse<PageResponseDTO<ProductResponseDTO>>> getAllProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "24") @Max(24) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setSearch(search);
        productQueryParams.setCategory(category);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        List<ProductResponseDTO> productResponseDTOS = productService.getProducts(productQueryParams);
        Integer total = productService.countProducts(productQueryParams);

        PageResponseDTO<ProductResponseDTO> pageResponseDTO = new PageResponseDTO<>();
        pageResponseDTO.setLimit(limit);
        pageResponseDTO.setOffset(offset);
        pageResponseDTO.setTotal(total);
        pageResponseDTO.setResults(productResponseDTOS);

        // 統一返回 HTTP 200
        return ResponseEntity.ok(ApiResponse.success("查詢成功", pageResponseDTO));
    }

    @PostMapping("/products/search/{productId}")
    @Operation(summary = "查詢單一商品", description = "根據商品 ID 查詢商品詳細資訊")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductById(@PathVariable Integer productId) {

        ProductResponseDTO productResponseDTO = productService.getProductById(productId);

        if (productResponseDTO != null) {
            // 找到商品：HTTP 200 + code 200
            return ResponseEntity.ok(ApiResponse.success("查詢成功", productResponseDTO));
        } else {
            // 找不到商品：HTTP 200 + code 4101
            return ResponseEntity.ok(ApiResponse.error(ApiCode.PRODUCT_NOT_FOUND));
        }
    }

    @PostMapping("/products/create")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "新增商品", description = "建立新商品（需要 JWT 認證）")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(
            @RequestBody @Valid ProductRequestDTO productRequestDTO) {

        Integer productId = productService.createProduct(productRequestDTO);
        ProductResponseDTO productResponseDTO = productService.getProductById(productId);

        // 統一返回 HTTP 200
        return ResponseEntity.ok(ApiResponse.success("商品新增成功", productResponseDTO));
    }

    @PostMapping("/products/update/{productId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "修改商品", description = "更新指定商品的資訊（需要 JWT 認證）")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @PathVariable Integer productId,
            @RequestBody @Valid ProductRequestDTO productRequestDTO) {

        ProductResponseDTO productResponseDTO = productService.getProductById(productId);

        // 找不到要修改的商品
        if (productResponseDTO == null) {
            // HTTP 200 + code 4101
            return ResponseEntity.ok(ApiResponse.error(ApiCode.PRODUCT_NOT_FOUND));
        }

        // 修改商品
        productService.updateProduct(productId, productRequestDTO);
        ProductResponseDTO updatedProductResponseDTO = productService.getProductById(productId);

        // 統一返回 HTTP 200
        return ResponseEntity.ok(ApiResponse.success("商品修改成功", updatedProductResponseDTO));
    }

    @PostMapping("/products/delete/{productId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "刪除商品", description = "刪除指定商品（需要 JWT 認證）")
    public ResponseEntity<ApiResponse<Void>> deleteProductById(@PathVariable Integer productId) {

        productService.deleteProductById(productId);

        // 統一返回 HTTP 200，無資料
        return ResponseEntity.ok(ApiResponse.success("商品刪除成功"));
    }
}