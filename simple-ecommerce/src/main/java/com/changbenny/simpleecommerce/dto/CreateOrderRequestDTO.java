package com.changbenny.simpleecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
public class CreateOrderRequestDTO {
    //不能是 null，也不能是空字串/空集合
    @NotEmpty
    private List<BuyItemDTO> buyItemDTOList;
}
