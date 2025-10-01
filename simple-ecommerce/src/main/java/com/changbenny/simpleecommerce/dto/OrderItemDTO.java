package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "訂單明細 DTO")
public class OrderItemDTO {

    @Schema(description = "訂單明細ID")
    private Integer orderItemId;

    @Schema(description = "商品ID")
    private Integer productId;

    @Schema(description = "商品名稱")
    private String productName;  // 從 @Transient 來的

    @Schema(description = "商品圖片")
    private String imageUrl;  // 從 @Transient 來的

    @Schema(description = "購買數量")
    private Integer quantity;

    @Schema(description = "小計金額")
    private BigDecimal amount;
}
