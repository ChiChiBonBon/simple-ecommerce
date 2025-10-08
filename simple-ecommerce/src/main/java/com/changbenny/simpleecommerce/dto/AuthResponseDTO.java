package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//產生以下
//Getter/Setter
//toString()
//equals() / hashCode()
//建構子
@Data
@Schema(description = "認證回應")
public class AuthResponseDTO {

    @Schema(description = "使用者 ID", example = "1")
    private Integer userId;  //查詢訂單用、建立訂單用

    @Schema(description = "使用者電子信箱", example = "user@example.com")
    private String email;

    @Schema(description = "JWT 存取權杖", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "權杖有效期限（秒）", example = "3600")
    private Long expiresIn;
}
