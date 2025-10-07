package com.changbenny.simpleecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "使用者註冊請求")
public class UserRegisterRequestDTO {

    @Schema(description = "電子信箱", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "電子信箱不能為空")
    @Email(message = "電子信箱格式錯誤")
    @Size(max = 100, message = "電子信箱不得超過 100 字元")
    @JsonProperty("email")//接收request或處理response的指定此json欄位
    private String email;

    @Schema(description = "密碼", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, max = 20, message = "密碼長度必須在 6-20 字元之間")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)// 只接收但不回傳
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
