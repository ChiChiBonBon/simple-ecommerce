package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Schema(description = "建立訂單請求")
public class CreateOrderRequestDTO {
    @Schema(description = "購買項目清單", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "購買項目不能為空")
    @Valid  //檢查 List 不是空的，也會檢查 每個 BuyItemDTO 內的所有驗證規則
    private List<BuyItemDTO> buyItemDTOList;
}
