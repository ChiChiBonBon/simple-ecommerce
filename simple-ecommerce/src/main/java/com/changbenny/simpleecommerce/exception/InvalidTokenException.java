package com.changbenny.simpleecommerce.exception;

import com.changbenny.simpleecommerce.constant.ApiCode;
import lombok.Getter;

@Getter
// 讓它繼承 RuntimeException，以便 Spring 可以捕獲
public class InvalidTokenException extends RuntimeException {

    private final ApiCode apiCode;
    
    // 1. 使用預設的 ApiCode 錯誤
    public InvalidTokenException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.apiCode = apiCode;
    }

    // 2. 使用自訂的詳細錯誤訊息
    public InvalidTokenException(ApiCode apiCode, String details) {
        super(details); // 將 details 作為 message 傳遞
        this.apiCode = apiCode;
    }
}