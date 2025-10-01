package com.changbenny.simpleecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Schema(description = "訂單回應 DTO")
public class OrderResponseDTO {

    @Schema(description = "訂單 ID", example = "1")
    private Integer orderId;

    @Schema(description = "使用者 ID", example = "1")
    private Integer userId;

    @Schema(description = "訂單總金額", example = "59800.00")
    private BigDecimal totalAmount;

    @Schema(description = "訂單明細列表")
    private List<OrderItemDTO> orderItemList;  // ← 這裡是 List

    @Schema(description = "最後修改時間", example = "2025-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private Date createdDate;

    @Schema(description = "最後修改時間")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private Date lastModifiedDate;
}
