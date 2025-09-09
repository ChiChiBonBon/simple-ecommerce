package com.changbenny.simpleecommerce.repository;

import com.changbenny.simpleecommerce.dto.ProductRequestDTO;
import com.changbenny.simpleecommerce.entity.ProductEntity;

import java.util.List;

public interface ProductRepository {
    List<ProductEntity> getProducts();

    ProductEntity getProductById(Integer productId);

    Integer createProduct(ProductRequestDTO productRequestDTO);

    void updateProduct(Integer productId, ProductRequestDTO productRequestDTO);

    void deleteProductById(Integer productId);
}
