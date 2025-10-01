package com.changbenny.simpleecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data//建setter和getter
@NoArgsConstructor//無參數建構子
@AllArgsConstructor//全參數建構子
@Schema(description = "使用者回應")
public class UserResponseDTO {
    @Schema(description = "使用者 ID", example = "1")
    private Integer userId;

    @Schema(description = "電子信箱", example = "user@example.com")
    @JsonProperty("e_mail")//接收request或處理response的指定此json欄位
    private String email;



    //密碼不可回傳
    //@JsonIgnore//request或response不處理此json欄位
    //private String password;

    @Schema(description = "帳號建立時間", example = "2025-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")//指定JSON序列化與反序列化的日期時間格式
    private Date createdDate;

    @Schema(description = "最後修改時間", example = "2025-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")//指定JSON序列化與反序列化的日期時間格式
    private Date lastModifiedDate;
}
