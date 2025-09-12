package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.constant.ProductCategory;
import com.changbenny.simpleecommerce.dto.ProductQueryParams;
import com.changbenny.simpleecommerce.dto.ProductRequestDTO;
import com.changbenny.simpleecommerce.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    Integer countProducts(ProductQueryParams productQueryParams);

    List<ProductEntity> getProducts(ProductQueryParams productQueryParams);

    ProductEntity getProductById(Integer productId);

    Integer createProduct(ProductRequestDTO productRequestDTO);

    void updateProduct(Integer productId, ProductRequestDTO productRequestDTO);

    void deleteProductById(Integer productId);

    void updateStock(Integer productId, Integer stock);
}
