package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "訂單明細 DTO")
public class OrderItemDTO {

    @Schema(description = "訂單明細 ID", example = "1")
    private Integer orderItemId;

    @Schema(description = "商品 ID", example = "1")
    private Integer productId;

    @Schema(description = "商品圖片 URL", example = "https://example.com/image.jpg")
    private String productName;  // 從 @Transient 來的

    @Schema(description = "商品圖片 URL", example = "https://example.com/image.jpg")
    private String imageUrl;  // 從 @Transient 來的

    @Schema(description = "購買數量", example = "2")
    private Integer quantity;

    @Schema(description = "小計金額", example = "59800.00")
    private BigDecimal amount;
}
