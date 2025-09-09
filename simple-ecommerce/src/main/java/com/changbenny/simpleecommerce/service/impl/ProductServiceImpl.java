package com.changbenny.simpleecommerce.service.impl;

import com.changbenny.simpleecommerce.dto.ProductRequestDTO;
import com.changbenny.simpleecommerce.entity.ProductEntity;
import com.changbenny.simpleecommerce.repository.ProductRepository;
import com.changbenny.simpleecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository  productRepository;

    @Override
    public List<ProductEntity> getProducts() {
        return productRepository.getProducts();
    }

    @Override
    public ProductEntity getProductById(Integer productId) {
        return  productRepository.getProductById(productId);
    }

    @Override
    public Integer createProduct(ProductRequestDTO productRequestDTO) {
        return productRepository.createProduct(productRequestDTO);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequestDTO productRequestDTO) {
        productRepository.updateProduct(productId,productRequestDTO);
    }

    @Override
    public void deleteProductById(Integer productId) {
        productRepository.deleteProductById(productId);
    }
}
