package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.constant.ProductCategory;
import com.changbenny.simpleecommerce.dto.ProductQueryParams;
import com.changbenny.simpleecommerce.dto.ProductRequestDTO;
import com.changbenny.simpleecommerce.entity.ProductEntity;
import com.changbenny.simpleecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    //查詢所有商品
    @PostMapping("/products")
    public ResponseEntity<List<ProductEntity>> getAllProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search
    ) {
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setSearch(search);
        productQueryParams.setCategory(category);

        List<ProductEntity> productEntityList = productService.getProducts(productQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(productEntityList);
    }

    //依商品ID查找
    @PostMapping("/products/search/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Integer productId) {
        ProductEntity productEntity = productService.getProductById(productId);

        if(productEntity != null){
            //回傳200狀態碼，前端請求成功並後端回傳結果
            return ResponseEntity.status(HttpStatus.OK).body(productEntity);
        }else{
            //回傳404狀態碼，前端請求的資源不存在
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //商品新增
    @PostMapping("/products/create")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        Integer productId = productService.createProduct(productRequestDTO);

        ProductEntity productEntity = productService.getProductById(productId);

        //回傳201狀態碼，後端已成功建立一個新資源
        return ResponseEntity.status(HttpStatus.CREATED).body(productEntity);
    }

    //商品修改
    @PostMapping("/products/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer productId,
                                           @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        ProductEntity productEntity = productService.getProductById(productId);

        //找不到要修改的商品，則回傳404
        if(productEntity == null){
            return ResponseEntity.notFound().build();
        }

        //修改商品
        productService.updateProduct(productId,productRequestDTO);

        ProductEntity updatedProductEntity = productService.getProductById(productId);

        //回傳200和修改後的商品資料
        return ResponseEntity.status(HttpStatus.OK).body(updatedProductEntity);
    }

    //商品刪除
    @PostMapping("/products/delete/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable Integer productId) {
        productService.deleteProductById(productId);

        //回傳204狀態碼，不回傳body
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
