package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.constant.ApiCode;
import com.changbenny.simpleecommerce.dto.*;
import com.changbenny.simpleecommerce.exception.InvalidTokenException;
import com.changbenny.simpleecommerce.service.PasswordResetService;
import com.changbenny.simpleecommerce.service.UserService;
import com.changbenny.simpleecommerce.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "使用者認證與授權 (Authentication)",
        description = "提供使用者註冊、登入/登出、密碼重設等相關的 API"
)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;

    public AuthController(UserService userService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }



    @PostMapping("/register")
    @Operation(summary = "使用者註冊", description = "創建新使用者帳號並返回登入憑證")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(
            @RequestBody @Valid UserRegisterRequestDTO userRegisterRequestDTO) {

        Integer userId = userService.register(userRegisterRequestDTO);
        UserResponseDTO userResponseDTO = userService.getUserById(userId);

        String token = JwtUtil.generate(userResponseDTO.getUserId(), userResponseDTO.getEmail());

        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setUserId(userResponseDTO.getUserId());//設置 userId
        authResponseDTO.setEmail(userResponseDTO.getEmail());
        authResponseDTO.setAccessToken(token);
        authResponseDTO.setExpiresIn(3600L);

        // 統一返回 HTTP 200
        return ResponseEntity.ok(ApiResponse.success("註冊成功", authResponseDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "使用者登入", description = "驗證帳號密碼並返回 JWT Token")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(
            @RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO) {

        // 帳密驗證：內部已做「email 是否存在」與「MD5 雜湊比對」
        UserResponseDTO userResponseDTO = userService.login(userLoginRequestDTO);

        // 簽發 JWT：以資料庫查出的 userId 與 email 為準
        String token = JwtUtil.generate(userResponseDTO.getUserId(), userResponseDTO.getEmail());

        // 回傳授權資訊（不要回密碼）
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setUserId(userResponseDTO.getUserId());  //設置 userId
        authResponseDTO.setEmail(userResponseDTO.getEmail());
        authResponseDTO.setAccessToken(token);
        authResponseDTO.setExpiresIn(3600L);

        // 統一返回 HTTP 200 + ApiResponse
        return ResponseEntity.ok(ApiResponse.success("登入成功", authResponseDTO));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "發送密碼重設郵件", description = "輸入註冊 Email，系統會發送包含重設連結的郵件")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequestDTO forgotPasswordRequest) {

        passwordResetService.processForgotPassword(forgotPasswordRequest.getEmail());

        // 為了安全性，無論 email 是否存在，都應回傳成功訊息，避免用戶探測系統 email
        return ResponseEntity.ok(
                ApiResponse.success("If a matching account is found, a password reset email has been sent.")
        );
    }

    @PostMapping("/reset-password")
    @Operation(summary = "執行密碼重設", description = "使用 Token 和新密碼完成密碼更新")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestBody @Valid ResetPasswordRequestDTO resetPasswordRequest) {

        try {
            passwordResetService.resetPassword(
                    resetPasswordRequest.getToken(),
                    resetPasswordRequest.getNewPassword()
            );

            // 成功：HTTP 200 + code 200
            return ResponseEntity.ok(ApiResponse.success("Password has been successfully reset."));

        } catch (InvalidTokenException e) {
            // 失敗：也返回 HTTP 200，但 code 是錯誤碼
            return ResponseEntity.ok(
                    ApiResponse.error(e.getApiCode(), e.getMessage())
            );
        }
    }

    @PostMapping("/change-password")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "修改密碼（需登入）", description = "已登入用戶修改自己的密碼")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody @Valid ChangePasswordRequestDTO changePasswordRequest,
            HttpServletRequest request) {

        // 從 JWT Filter 設置的 attribute 取得 userId
        Integer userId = (Integer) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.ok(
                    ApiResponse.error(ApiCode.UNAUTHORIZED, "未授權")
            );
        }

        try {
            // 1. 取得用戶資料
            UserResponseDTO user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.ok(
                        ApiResponse.error(ApiCode.USER_NOT_FOUND)
                );
            }

            // 2. 驗證當前密碼
            boolean isCurrentPasswordValid = userService.verifyPassword(
                    user.getUserId(),
                    changePasswordRequest.getCurrentPassword()
            );

            if (!isCurrentPasswordValid) {
                return ResponseEntity.ok(
                        ApiResponse.error(ApiCode.INVALID_CREDENTIALS, "當前密碼錯誤")
                );
            }

            // 3. 更新密碼
            userService.updatePassword(
                    user.getUserId(),
                    changePasswordRequest.getNewPassword()
            );

            return ResponseEntity.ok(ApiResponse.success("密碼修改成功"));

        } catch (Exception e) {
            return ResponseEntity.ok(
                    ApiResponse.error(ApiCode.SERVER_ERROR, "修改密碼失敗: " + e.getMessage())
            );
        }
    }
}