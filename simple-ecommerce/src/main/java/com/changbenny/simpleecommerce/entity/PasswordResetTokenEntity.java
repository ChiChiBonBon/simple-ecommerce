package com.changbenny.simpleecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Data
public class PasswordResetTokenEntity {

    @Id
    @Column(nullable = false, length = 255)
    private String token; // token 作為主鍵

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}