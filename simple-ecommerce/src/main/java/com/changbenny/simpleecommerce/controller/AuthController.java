package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.util.JwtUtil;
import com.changbenny.simpleecommerce.dto.*;
import com.changbenny.simpleecommerce.entity.UserEntity;
import com.changbenny.simpleecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    public AuthController(UserService userService){ this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid UserRegisterRequestDTO userRegisterRequestDTO){
        Integer userId = userService.register(userRegisterRequestDTO);
        UserEntity userEntity = userService.getUserById(userId);

        String token = JwtUtil.generate(userEntity.getUserId(), userEntity.getEmail());
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setEmail(userEntity.getEmail());
        authResponseDTO.setAccessToken(token);
        authResponseDTO.setExpiresIn(3600L);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO) {
        //帳密驗證：內部已做「email 是否存在」與「MD5 雜湊比對」
        UserEntity userEntity = userService.login(userLoginRequestDTO);

        //簽發 JWT：以資料庫查出的 userId 與 email 為準
        String token = JwtUtil.generate(userEntity.getUserId(), userEntity.getEmail());

        //回傳授權資訊（不要回密碼）
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setEmail(userEntity.getEmail());
        authResponseDTO.setAccessToken(token);
        authResponseDTO.setExpiresIn(3600L); //1小時（依JwtUtil 設定調整）
        return ResponseEntity.ok(authResponseDTO);
    }
}
