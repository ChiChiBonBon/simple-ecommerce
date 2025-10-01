package com.changbenny.simpleecommerce.dto;

import com.changbenny.simpleecommerce.constant.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "商品查詢參數")
public class ProductQueryParams {
    @Schema(description = "商品分類", example = "FOOD")
    private ProductCategory category;

    @Schema(description = "搜尋關鍵字", example = "iPhone")
    private String search;

    @Schema(description = "排序欄位", example = "created_date")
    private String orderBy;

    @Schema(description = "排序方式", example = "desc")
    private String sort;

    @Schema(description = "每頁筆數", example = "10")
    private Integer limit;

    @Schema(description = "跳過筆數", example = "0")
    private Integer offset;

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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
