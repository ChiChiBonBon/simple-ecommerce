package com.changbenny.simpleecommerce.repository;

import com.changbenny.simpleecommerce.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, String> {

    // 刪除某使用者的所有token
    @Transactional
    void deleteByUserId(Integer userId);

    // 刪除過期的token
    @Transactional
    void deleteByExpiryDateBefore(LocalDateTime now);
}