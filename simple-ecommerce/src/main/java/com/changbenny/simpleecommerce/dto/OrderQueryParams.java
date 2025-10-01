package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "訂單查詢參數")
public class OrderQueryParams {

    @Schema(description = "使用者 ID", example = "1")
    private Integer userId;

    @Schema(description = "每頁筆數", example = "10", minimum = "0", maximum = "24")
    private Integer limit;

    @Schema(description = "跳過筆數", example = "0", minimum = "0")
    private Integer offset;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}