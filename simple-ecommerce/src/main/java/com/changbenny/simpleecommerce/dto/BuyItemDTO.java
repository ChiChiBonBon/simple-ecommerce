package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BuyItemDTO {
    @Schema(description = "商品 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer productId;

    @Schema(description = "購買數量", example = "2", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1")
    @NotNull
    @Min(1)//至少購買數量是1
    @Max(999)//防止惡意輸入數量
    private Integer quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
