package com.changbenny.simpleecommerce.dto;

import com.changbenny.simpleecommerce.constant.ProductCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "商品回應 DTO")
public class ProductResponseDTO {

    @Schema(description = "商品ID", example = "101")
    private Integer productId;

    @Schema(description = "商品名稱", example = "焦糖布丁")
    private String productName;

    @Schema(description = "商品分類", example = "FOOD")
    private ProductCategory category;

    @Schema(description = "商品圖片 URL", example = "https://example.com/images/pudding.png")
    private String imageUrl;

    @Schema(description = "價格", example = "199.00")
    private BigDecimal price;

    @Schema(description = "庫存", example = "50")
    private Integer stock;

    @Schema(description = "商品描述", example = "香濃滑順的焦糖布丁")
    private String description;

    @Schema(description = "建立時間", example = "2025-09-24T12:34:56")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private Date createdDate;  // ✅ 後台可能需要追蹤

    @Schema(description = "最後修改時間", example = "2025-09-25T08:12:34")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private Date lastModifiedDate;
}