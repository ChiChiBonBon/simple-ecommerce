package com.changbenny.simpleecommerce.repository.impl;

import com.changbenny.simpleecommerce.entity.ProductEntity;
import com.changbenny.simpleecommerce.repository.ProductRepository;
import com.changbenny.simpleecommerce.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public ProductEntity getProductById(Integer productId) {
        String sqlString = " select product_id, product_name, category, image_url, price, stock, description, " +
                           " created_date, last_modified_date " +
                           " from product p " +
                           " where product_id = :productId ";

        Map<String,Object> productMap = new HashMap<>();
        productMap.put("productId", productId);

        List<ProductEntity> productEntityList = namedParameterJdbcTemplate.query(sqlString,productMap,new ProductRowMapper());

        //檢查List是否為Empty
        if(!productEntityList.isEmpty()){
            return productEntityList.get(0);
        }else {
            return null;
        }
    }
}
