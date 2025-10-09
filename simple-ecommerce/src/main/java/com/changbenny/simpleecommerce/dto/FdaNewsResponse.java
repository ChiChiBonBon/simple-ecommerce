package com.changbenny.simpleecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "食藥署新聞（前端用英文鍵）")
public class FdaNewsResponse {

    @Schema(description = "新聞標題", example = "「百威食品有限公司」疑慮豬腸已監督下架")
    private String title;

    @Schema(description = "新聞內容 (HTML)")
    private String content;

    @Schema(description = "純文字摘要（供列表顯示）", example = "食藥署督導地方衛生局監督疑慮產品下架...")
    private String contentText;

    @Schema(description = "附件連結陣列",
            example = "[\"https://www.fda.gov.tw/TC/includes/GetFile.ashx?id=t398513\",\"https://www.fda.gov.tw/TC/includes/GetFile.ashx?id=t398514\"]")
    private List<String> attachments = List.of(); // 預設非 null

    @Schema(description = "發布日期 (yyyy/MM/dd)", example = "2025/10/05")
    private String publishDate;
}
