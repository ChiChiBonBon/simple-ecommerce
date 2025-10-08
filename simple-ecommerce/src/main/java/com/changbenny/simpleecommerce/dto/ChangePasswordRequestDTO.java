package com.changbenny.simpleecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "修改密碼請求")
public class ChangePasswordRequestDTO {

    @NotBlank(message = "當前密碼不能為空")
    @Schema(description = "當前密碼", example = "oldpassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currentPassword;

    @NotBlank(message = "新密碼不能為空")
    @Size(min = 6, message = "新密碼至少需要 6 個字元")
    @Schema(description = "新密碼", example = "newpassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
}
