package com.changbenny.simpleecommerce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 發送密碼重置信件
     */
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@simpleecommerce.com");
            message.setTo(toEmail);
            message.setSubject("【Simple E-commerce】密碼重置請求");
            message.setText(buildResetEmailContent(toEmail, resetToken));

            mailSender.send(message);
            logger.info("密碼重置信件已發送至: {}", toEmail);

        } catch (Exception e) {
            logger.error("發送信件失敗: {}", e.getMessage(), e);
            throw new RuntimeException("信件發送失敗，請稍後再試");
        }
    }

    /**
     * 建構信件內容
     */
    private String buildResetEmailContent(String email, String token) {
        return String.format("""
            您好，
            
            我們收到了您（%s）的密碼重置請求。
            
            您的重置token是：
            %s
            
            請在 30 分鐘內使用此token重置密碼。
            
            如果您沒有請求重置密碼，請忽略此信件。
            
            此信件由系統自動發送，請勿回覆。
            
            ---
            Simple E-commerce Team
            """, email, token);
    }
}
