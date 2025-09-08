package com.changbenny.simpleecommerce.rowmapper;

import com.changbenny.simpleecommerce.entity.ProductEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<ProductEntity> {
    @Override
    public ProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(rs.getInt("product_id"));
        productEntity.setProductName(rs.getString("product_name"));
        productEntity.setCategory(rs.getString("category"));
        productEntity.setImageUrl(rs.getString("image_url"));
        productEntity.setPrice(rs.getBigDecimal("price"));
        productEntity.setDescription(rs.getString("description"));
        productEntity.setCreatedDate(rs.getDate("created_date"));
        productEntity.setLastModifiedDate(rs.getDate("last_modified_date"));
        return productEntity;
    }
}
