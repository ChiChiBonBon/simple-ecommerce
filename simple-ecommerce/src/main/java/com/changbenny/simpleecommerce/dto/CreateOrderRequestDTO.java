package com.changbenny.simpleecommerce.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateOrderRequestDTO {
    //不能是 null，也不能是空字串/空集合
    @NotEmpty
    private List<BuyItemDTO> buyItemDTOList;

    public List<BuyItemDTO> getBuyItemDTOList() {
        return buyItemDTOList;
    }

    public void setBuyItemDTOList(List<BuyItemDTO> buyItemDTOList) {
        this.buyItemDTOList = buyItemDTOList;
    }
}
