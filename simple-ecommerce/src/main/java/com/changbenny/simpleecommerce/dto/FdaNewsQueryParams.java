package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "食藥署新聞查詢參數")
public class FdaNewsQueryParams  {
    @Schema(description = "標題關鍵字", example = "食品")
    private String keyword;

    @Schema(description = "發布日期起始時間", example = "2016/01/01")
    private String startdate;

    @Schema(description = "發布日期結束時間", example = "2026/12/31")
    private String enddate;
}
