package com.changbenny.simpleecommerce.constant;

import lombok.Getter;

@Getter
public enum ApiCode {
    // 成功
    SUCCESS(200, "操作成功"),

    // 使用者相關錯誤 40xx
    USER_NOT_FOUND(4001, "使用者不存在"),
    USER_ALREADY_EXISTS(4002, "該 email 已被註冊"),
    INVALID_CREDENTIALS(4003, "帳號或密碼錯誤"),
    INVALID_TOKEN(4004, "無效的 Token"),
    TOKEN_EXPIRED(4005, "Token 已過期"),

    // 商品相關錯誤 41xx
    PRODUCT_NOT_FOUND(4101, "商品不存在"),
    INSUFFICIENT_STOCK(4102, "庫存不足"),
    INVALID_PRODUCT_DATA(4103, "商品資料格式錯誤"),

    // 訂單相關錯誤 42xx
    ORDER_NOT_FOUND(4201, "訂單不存在"),
    INVALID_ORDER_DATA(4202, "訂單資料格式錯誤"),
    ORDER_CANNOT_BE_CANCELLED(4203, "訂單無法取消"),

    // 請求錯誤 43xx
    BAD_REQUEST(4300, "請求參數錯誤"),
    VALIDATION_ERROR(4301, "資料驗證失敗"),
    MISSING_REQUIRED_FIELD(4302, "缺少必填欄位"),

    // 權限相關錯誤 44xx
    UNAUTHORIZED(4401, "未授權，請先登入"),
    FORBIDDEN(4403, "沒有權限執行此操作"),

    // 伺服器錯誤 50xx
    SERVER_ERROR(5000, "系統錯誤，請稍後再試"),
    DATABASE_ERROR(5001, "資料庫錯誤"),
    EXTERNAL_API_ERROR(5002, "外部 API 呼叫失敗");

    private final Integer code;
    private final String message;

    ApiCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
