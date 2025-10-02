package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.dto.ApiResponse;
import com.changbenny.simpleecommerce.dto.UserLoginRequestDTO;
import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.dto.UserResponseDTO;
import com.changbenny.simpleecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "使用者管理 (User)", description = "使用者註冊、登入相關 API（不含 JWT）")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    @Operation(summary = "使用者註冊", description = "建立新使用者帳號")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(
            @RequestBody @Valid UserRegisterRequestDTO userRegisterRequestDTO) {

        Integer userId = userService.register(userRegisterRequestDTO);
        UserResponseDTO userResponseDTO = userService.getUserById(userId);

        // 統一返回 HTTP 200
        return ResponseEntity.ok(ApiResponse.success("註冊成功", userResponseDTO));
    }

    @PostMapping("/users/login")
    @Operation(summary = "使用者登入", description = "驗證帳號密碼並返回使用者資訊")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(
            @RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO) {

        UserResponseDTO userResponseDTO = userService.login(userLoginRequestDTO);

        // 統一返回 HTTP 200
        return ResponseEntity.ok(ApiResponse.success("登入成功", userResponseDTO));
    }
}