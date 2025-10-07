package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//用於接收使用者請求重設密碼的email
@Data
// 定義整個 DTO 在 Swagger 中的名稱和說明
@Schema(description = "密碼重設請求 - 傳入 Email")
public class ForgotPasswordRequestDTO {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    // 為欄位加上 Schema，提供範例值和詳細說明
    @Schema(description = "註冊 Email 地址", example = "user@example.com")
    private String email;
}
