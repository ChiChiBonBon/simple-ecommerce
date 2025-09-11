package com.changbenny.simpleecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data//建setter和getter
@NoArgsConstructor//無參數建構子
@AllArgsConstructor//全參數建構子
public class UserResponseDTO {
    @JsonProperty("e_mail")//接收request或處理response的指定此json欄位
    private String email;

    @JsonIgnore//request或response不處理此json欄位
    private String password;

    //指定JSON序列化與反序列化的日期時間格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    //指定JSON序列化與反序列化的日期時間格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifiedDate;
}
