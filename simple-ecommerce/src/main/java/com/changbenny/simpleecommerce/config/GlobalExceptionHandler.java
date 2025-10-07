package com.changbenny.simpleecommerce.config;

import com.changbenny.simpleecommerce.dto.ApiResponse;
import com.changbenny.simpleecommerce.exception.InvalidTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidToken(InvalidTokenException ex) {
        // 统一返回 HTTP 200
        return ResponseEntity.ok(
                ApiResponse.error(ex.getApiCode(), ex.getMessage())
        );
    }
}
