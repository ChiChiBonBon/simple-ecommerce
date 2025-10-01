package com.changbenny.simpleecommerce.service.impl;

import com.changbenny.simpleecommerce.dto.ProductQueryParams;
import com.changbenny.simpleecommerce.dto.ProductRequestDTO;
import com.changbenny.simpleecommerce.dto.ProductResponseDTO;
import com.changbenny.simpleecommerce.entity.ProductEntity;
import com.changbenny.simpleecommerce.repository.ProductRepository;
import com.changbenny.simpleecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository  productRepository;

    @Override
    public Integer countProducts(ProductQueryParams productQueryParams) {
        return productRepository.countProducts(productQueryParams);
    }

    @Override
    public List<ProductResponseDTO> getProducts(ProductQueryParams productQueryParams) {
        return convertToDTOList(productRepository.getProducts(productQueryParams));
    }

    @Override
    public ProductResponseDTO getProductById(Integer productId) {
        return  convertToDTO(productRepository.getProductById(productId));
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

    @Override
    public void updateStock(Integer productId, Integer stock) {
        productRepository.updateStock(productId,stock);
    }

    public ProductResponseDTO convertToDTO(ProductEntity entity) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(entity.getProductId());
        dto.setProductName(entity.getProductName());
        dto.setCategory(entity.getCategory());
        dto.setImageUrl(entity.getImageUrl());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setDescription(entity.getDescription());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        return dto;
    }

    @Override
    public List<ProductResponseDTO> convertToDTOList(List<ProductEntity> productEntityList) {
        return productEntityList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
