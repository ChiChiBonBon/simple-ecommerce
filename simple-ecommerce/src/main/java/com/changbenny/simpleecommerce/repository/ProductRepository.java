package com.changbenny.simpleecommerce.repository;

import com.changbenny.simpleecommerce.entity.ProductEntity;

public interface ProductRepository {
    ProductEntity getProductById(Integer productId);
}
