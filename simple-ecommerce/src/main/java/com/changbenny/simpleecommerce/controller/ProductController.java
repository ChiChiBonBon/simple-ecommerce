package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.entity.ProductEntity;
import com.changbenny.simpleecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/products/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Integer productId) {
        ProductEntity productEntity = productService.getProductById(productId);

        if(productEntity != null){
            return ResponseEntity.status(HttpStatus.OK).body(productEntity);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
