package com.changbenny.simpleecommerce.dto;

import com.changbenny.simpleecommerce.constant.ProductCategory;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Data//建setter和getter
@NoArgsConstructor//無參數建構子
@AllArgsConstructor//全參數建構子
public class ProductRequestDTO {
    //前端送值不可為Null
    @NotNull
    private String productName;
    //前端送值不可為Null
    @NotNull
    private ProductCategory category;
    //前端送值不可為Null
    @NotNull
    private String imageUrl;
    //前端送值不可為Null
    @NotNull
    private BigDecimal price;
    //前端送值不可為Null
    @NotNull
    private Integer stock;

    private String description;
}
