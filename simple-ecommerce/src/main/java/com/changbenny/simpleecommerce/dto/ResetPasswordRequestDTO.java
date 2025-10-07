package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
// 定義整個 DTO 在 Swagger 中的名稱和說明
@Schema(description = "密碼重設 - 傳入 Token 與新密碼")
public class ResetPasswordRequestDTO {

    /**
     * 從 URL 傳遞過來的密碼重設 Token
     */
    @NotBlank(message = "Token cannot be empty")
    @Schema(description = "郵件中取得的密碼重設 Token", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private String token;

    /**
     * 使用者輸入的新密碼
     */
    @NotBlank(message = "New password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "使用者設定的新密碼", example = "newsecurepassword123")
    private String newPassword;
}
