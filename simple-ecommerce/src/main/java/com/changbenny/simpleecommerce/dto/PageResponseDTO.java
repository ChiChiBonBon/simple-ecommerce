package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

//ResponseDTO
@Schema(description = "分頁回應")
public class PageResponseDTO<T> {
    @Schema(description = "每頁筆數", example = "10")
    private Integer limit;

    @Schema(description = "跳過筆數", example = "0")
    private Integer offset;

    @Schema(description = "總筆數", example = "100")
    private Integer total;

    @Schema(description = "資料列表")
    private List<T> results;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
