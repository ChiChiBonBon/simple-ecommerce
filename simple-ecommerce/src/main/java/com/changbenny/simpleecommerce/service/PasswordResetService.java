package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.entity.PasswordResetTokenEntity;
import com.changbenny.simpleecommerce.entity.UserEntity;
import com.changbenny.simpleecommerce.repository.PasswordResetTokenRepository;
import com.changbenny.simpleecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private static final int TOKEN_VALIDITY_MINUTES = 30;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(
            UserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 處理忘記密碼請求
     */
    @Transactional
    public void processForgotPassword(String email) {
        // 1. 查找使用者（返回 UserEntity，不是 Optional）
        UserEntity user = userRepository.getUserByEmail(email);

        // 2. 手動檢查是否為 null
        if (user == null) {
            throw new RuntimeException("該信箱未註冊");
        }

        // 3. 刪除該使用者的舊token
        tokenRepository.deleteByUserId(user.getUserId());

        // 4. 生成新token
        String token = UUID.randomUUID().toString();

        // 5. 儲存token
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        resetToken.setToken(token);
        resetToken.setUserId(user.getUserId());
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_MINUTES));
        resetToken.setCreatedDate(LocalDateTime.now());
        tokenRepository.save(resetToken);

        // 6. 發送信件
        emailService.sendPasswordResetEmail(email, token);

        logger.info("使用者 {} 請求密碼重置", email);
    }

    /**
     * 重置密碼
     */
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // 1. 查找token
        PasswordResetTokenEntity resetToken = tokenRepository.findById(token)
                .orElseThrow(() -> new RuntimeException("無效的重置token"));

        // 2. 檢查是否過期
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("token已過期，請重新申請");
        }

        // 3. 查找使用者
        UserEntity userEntity = userRepository.getUserById(resetToken.getUserId());
        if (userEntity == null) {
            throw new RuntimeException("使用者不存在");
        }

        // 4. 更新密碼
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.saveUser(userEntity);

        // 5. 刪除已使用的token
        tokenRepository.delete(resetToken);

        logger.info("使用者 {} 成功重置密碼", userEntity.getEmail());
    }

    /**
     * 清理過期token（可設定排程定時執行）
     */
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
        logger.info("已清理過期的密碼重置token");
    }
}