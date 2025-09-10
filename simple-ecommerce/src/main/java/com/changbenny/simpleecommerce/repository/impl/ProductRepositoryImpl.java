package com.changbenny.simpleecommerce.repository.impl;

import com.changbenny.simpleecommerce.constant.ProductCategory;
import com.changbenny.simpleecommerce.dto.ProductQueryParams;
import com.changbenny.simpleecommerce.dto.ProductRequestDTO;
import com.changbenny.simpleecommerce.entity.ProductEntity;
import com.changbenny.simpleecommerce.repository.ProductRepository;
import com.changbenny.simpleecommerce.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countProducts(ProductQueryParams productQueryParams) {
        String sqlString = " SELECT COUNT(*)" +
                " FROM product WHERE 1=1 ";

        Map<String, Object> productMap = new HashMap<>();

        //依商品查詢的商品分類為條件
        if(productQueryParams.getCategory() != null){
            sqlString += " AND category = :category ";
            productMap.put("category", productQueryParams.getCategory().name());
        }

        //依商品查詢的搜尋字串為條件
        if(productQueryParams.getSearch() != null ){
            sqlString += " AND product_name LIKE :search ";
            productMap.put("search","%" + productQueryParams.getSearch() + "%" );
        }

        //依欄位為排序依據
        sqlString += " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        //依分頁依據
        sqlString += " LIMIT :limit OFFSET :offset ";
        productMap.put("limit", productQueryParams.getLimit());
        productMap.put("offset", productQueryParams.getOffset());

        //執行查詢並回傳單一結果
        Integer total = namedParameterJdbcTemplate.queryForObject(sqlString, productMap, Integer.class);

        return total;
    }

    @Override
    public List<ProductEntity> getProducts(ProductQueryParams productQueryParams) {
        String sqlString = " SELECT price, product_id, stock, created_date, last_modified_date, product_name, " +
                           " image_url, description, category " +
                           " FROM product WHERE 1=1 ";

        Map<String, Object> productMap = new HashMap<>();

        //依商品查詢的商品分類為條件
        if(productQueryParams.getCategory() != null){
            sqlString += " AND category = :category ";
            productMap.put("category", productQueryParams.getCategory().name());
        }

        //依商品查詢的搜尋字串為條件
        if(productQueryParams.getSearch() != null ){
            sqlString += " AND product_name LIKE :search ";
            productMap.put("search","%" + productQueryParams.getSearch() + "%" );
        }

        //依欄位為排序依據
        sqlString += " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        //依分頁依據
        sqlString += " LIMIT :limit OFFSET :offset ";
        productMap.put("limit", productQueryParams.getLimit());
        productMap.put("offset", productQueryParams.getOffset());

        List<ProductEntity> productEntityList = namedParameterJdbcTemplate.query(sqlString, productMap, new ProductRowMapper());
        return productEntityList;
    }

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

    @Override
    public Integer createProduct(ProductRequestDTO productRequestDTO) {
        String sqlString = " INSERT INTO product " +
                " (product_name, category, image_url, price, stock, " +
                "  description, created_date, last_modified_date) " +
                " VALUES (:productName,:category,:imageUrl,:price,:stock," +
                "  :description,:createdDate,:lastModifiedDate) ";

        Map<String,Object> productMap = new HashMap<>();
        productMap.put("productName", productRequestDTO.getProductName());
        productMap.put("category", productRequestDTO.getCategory().name());
        productMap.put("imageUrl", productRequestDTO.getImageUrl());
        productMap.put("price", productRequestDTO.getPrice());
        productMap.put("stock", productRequestDTO.getStock());
        productMap.put("description", productRequestDTO.getDescription());

        Date date = new Date();

        productMap.put("createdDate", date);
        productMap.put("lastModifiedDate", date);


        //KeyHolder存放資料庫產生的主鍵
        //自動產生的 key 塞到 GeneratedKeyHolder，為KeyHolder的實作類別
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sqlString, new MapSqlParameterSource(productMap), keyHolder);

        //取得SQL執行後，自動生成的主鍵
        int productId = keyHolder.getKey().intValue();

        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequestDTO productRequestDTO) {
        String sqlString = " UPDATE product " +
                           " SET price= :price, stock= :stock, " +
                           " last_modified_date= :lastModifiedDate, " +
                           " product_name= :productName, image_url= :imageUrl, description= :description, category= :category " +
                           " WHERE product_id= :productId ";

        Map<String,Object> productMap = new HashMap<>();
        productMap.put("productId", productId);
        productMap.put("price", productRequestDTO.getPrice());
        productMap.put("stock", productRequestDTO.getStock());
        productMap.put("description", productRequestDTO.getDescription());
        productMap.put("productName", productRequestDTO.getProductName());
        productMap.put("imageUrl", productRequestDTO.getImageUrl());
        productMap.put("category", productRequestDTO.getCategory().name());
        //商品資料當下修改時間
        productMap.put("lastModifiedDate",new Date());

        namedParameterJdbcTemplate.update(sqlString,productMap);
    }

    @Override
    public void deleteProductById(Integer productId) {
        String sqlString = " DELETE FROM product WHERE product_id= :productId ";

        Map<String,Object> productMap = new HashMap<>();
        productMap.put("productId", productId);
        namedParameterJdbcTemplate.update(sqlString,productMap);
    }
}
