package com.changbenny.simpleecommerce.dto;

import com.changbenny.simpleecommerce.constant.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Data//建setter和getter
@NoArgsConstructor//無參數建構子
@AllArgsConstructor//全參數建構子
@Schema(description = "商品新增/修改請求")
public class ProductRequestDTO {
    @Schema(description = "商品名稱", example = "iPhone 15", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "商品名稱不能為空")  // 改用 @NotBlank
    @Size(max = 100, message = "商品名稱不得超過 100 字元")
    private String productName;

    @Schema(description = "商品分類", example = "FOOD", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品分類不能為空")
    private ProductCategory category;

    @Schema(description = "商品圖片 URL", example = "https://example.com/image.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "商品圖片不能為空")
    @Size(max = 500, message = "圖片 URL 不得超過 500 字元")
    private String imageUrl;


    @Schema(description = "商品價格", example = "29900.00", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    @NotNull(message = "商品價格不能為空")
    @Min(value = 0, message = "商品價格不能為負數")
    private BigDecimal price;

    @Schema(description = "庫存數量", example = "100", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    @NotNull(message = "庫存數量不能為空")
    @Min(value = 0, message = "庫存數量不能為負數")
    private Integer stock;


    @Schema(description = "商品描述", example = "全新 iPhone 15，128GB 容量")
    @Size(max = 1000, message = "商品描述不得超過 1000 字元")
    private String description;
}
