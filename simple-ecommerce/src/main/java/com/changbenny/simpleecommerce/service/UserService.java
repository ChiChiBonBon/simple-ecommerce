package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.dto.UserLoginRequestDTO;
import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.dto.UserResponseDTO;
import com.changbenny.simpleecommerce.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    Integer register(UserRegisterRequestDTO userRegisterRequestDTO);

    UserResponseDTO getUserById(Integer userId);

    UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);

    /**
     * 驗證用戶的密碼是否正確
     * @param userId 用戶ID
     * @param rawPassword 原始密碼（未加密）
     * @return 密碼是否正確
     */
    boolean verifyPassword(Integer userId, String rawPassword);

    /**
     * 更新用戶密碼
     * @param userId 用戶ID
     * @param newPassword 新密碼（會自動加密）
     */
    void updatePassword(Integer userId, String newPassword);
}
