package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.entity.ProductEntity;

public interface ProductService {
    ProductEntity getProductById(Integer productId);
}
