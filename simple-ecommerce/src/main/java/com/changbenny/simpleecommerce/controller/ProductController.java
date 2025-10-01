package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.constant.ProductCategory;
import com.changbenny.simpleecommerce.dto.PageResponseDTO;
import com.changbenny.simpleecommerce.dto.ProductQueryParams;
import com.changbenny.simpleecommerce.dto.ProductRequestDTO;
import com.changbenny.simpleecommerce.dto.ProductResponseDTO;
import com.changbenny.simpleecommerce.entity.ProductEntity;
import com.changbenny.simpleecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    //查詢所有商品
    @PostMapping("/products")
    public ResponseEntity<PageResponseDTO<ProductResponseDTO>> getAllProducts(
            //查詢條件，非必填
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,
            //排序，預設為created_date、desc
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,
            //分頁
            @RequestParam(defaultValue = "24") @Max(24) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setSearch(search);
        productQueryParams.setCategory(category);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        List<ProductResponseDTO> productResponseDTOS = productService.getProducts(productQueryParams);

        Integer total = productService.countProducts(productQueryParams);

        // 改用 ProductResponseDTO
        PageResponseDTO<ProductResponseDTO> pageResponseDTO = new PageResponseDTO<>();
        pageResponseDTO.setLimit(limit);
        pageResponseDTO.setOffset(offset);
        pageResponseDTO.setTotal(total);
        pageResponseDTO.setResults(productResponseDTOS);

        return ResponseEntity.status(HttpStatus.OK).body(pageResponseDTO);
    }

    //依商品ID查找
    @PostMapping("/products/search/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer productId) {
        //查詢到的entity轉換dto
        ProductResponseDTO productResponseDTO = productService.getProductById(productId);

        if(productResponseDTO != null){
            //回傳200狀態碼，前端請求成功並後端回傳結果
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
        }else{
            //回傳404狀態碼，前端請求的資源不存在
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //商品新增
    @PostMapping("/products/create")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        Integer productId = productService.createProduct(productRequestDTO);

        ProductResponseDTO productResponseDTO = productService.getProductById(productId);

        //回傳201狀態碼，後端已成功建立一個新資源
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    //商品修改
    @PostMapping("/products/update/{productId}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Integer productId,
                                           @RequestBody @Valid ProductRequestDTO productRequestDTO) {

        ProductResponseDTO productResponseDTO = productService.getProductById(productId);

        //找不到要修改的商品，則回傳404
        if(productResponseDTO == null){
            return ResponseEntity.notFound().build();
        }

        //修改商品
        productService.updateProduct(productId,productRequestDTO);

        ProductResponseDTO updatedProductResponseDTO = productService.getProductById(productId);

        //回傳200和修改後的商品資料
        return ResponseEntity.status(HttpStatus.OK).body(updatedProductResponseDTO);
    }

    //商品刪除
    @PostMapping("/products/delete/{productId}")
    @SecurityRequirement(name = "JWT")
    //Void表示明確表示沒有 body
    public ResponseEntity<Void> deleteProductById(@PathVariable Integer productId) {
        productService.deleteProductById(productId);

        //回傳204狀態碼，不回傳body
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


