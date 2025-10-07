package com.changbenny.simpleecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "食藥署新聞")
public class FdaNewsDTO {

    @Schema(description = "新聞標題")
    @JsonProperty("標題")
    private String title;

    @Schema(description = "新聞內容")
    @JsonProperty("內容")
    private String content;

    @Schema(description = "附檔連結")
    @JsonProperty("附檔連結")
    private String attachments;

    @Schema(description = "發布日期")
    @JsonProperty("發布日期")
    private String publishDate;
}
