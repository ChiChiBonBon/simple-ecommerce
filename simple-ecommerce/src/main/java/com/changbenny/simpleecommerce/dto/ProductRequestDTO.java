package com.changbenny.simpleecommerce.dto;

import com.changbenny.simpleecommerce.constant.ProductCategory;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Data//建setter和getter
@NoArgsConstructor//無參數建構子
@AllArgsConstructor//全參數建構子
public class ProductRequestDTO {
    //前端送值不可為Null
    @NonNull
    private String productName;
    //前端送值不可為Null
    @NonNull
    private ProductCategory category;
    //前端送值不可為Null
    @NonNull
    private String imageUrl;
    //前端送值不可為Null
    @NonNull
    private BigDecimal price;
    //前端送值不可為Null
    @NonNull
    private Integer stock;

    private String description;
}
