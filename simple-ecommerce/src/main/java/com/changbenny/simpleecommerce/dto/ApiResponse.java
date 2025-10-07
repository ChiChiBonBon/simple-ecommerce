package com.changbenny.simpleecommerce.dto;

import com.changbenny.simpleecommerce.constant.ApiCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "統一回應格式")
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 欄位不回傳
public class ApiResponse<T> {

    @Schema(description = "狀態碼", example = "200")
    private Integer code;

    @Schema(description = "訊息", example = "操作成功")
    private String message;

    @Schema(description = "資料內容")
    private T data;

    @Schema(description = "錯誤詳細資訊（僅失敗時出現）")
    private String details;

    @Schema(description = "時間戳記", example = "2025-10-01T12:00:00")
    private String timestamp;

    // 私有建構子
    private ApiResponse() {
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    // ========== 成功回應 ==========

    /**
     * 成功回應（無資料）
     */
    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ApiCode.SUCCESS.getCode());
        response.setMessage(ApiCode.SUCCESS.getMessage());
        return response;
    }

    /**
     * 成功回應（有資料）
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ApiCode.SUCCESS.getCode());
        response.setMessage(ApiCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    /**
     * 成功回應（自訂訊息，但無資料）
     * 專門用於方法簽名為 ApiResponse<Void> 時，提供自訂訊息
     */
    public static <T> ApiResponse<T> success(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ApiCode.SUCCESS.getCode());
        response.setMessage(message);
        // data 欄位將保持 null，符合 Void 的要求
        return response;
    }

    /**
     * 成功回應（自訂訊息 + 資料）
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ApiCode.SUCCESS.getCode());
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    // ========== 失敗回應 ==========

    /**
     * 失敗回應（使用 ApiCode）
     */
    public static <T> ApiResponse<T> error(ApiCode apiCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(apiCode.getCode());
        response.setMessage(apiCode.getMessage());
        return response;
    }

    /**
     * 失敗回應（ApiCode + 詳細錯誤）
     */
    public static <T> ApiResponse<T> error(ApiCode apiCode, String details) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(apiCode.getCode());
        response.setMessage(apiCode.getMessage());
        response.setDetails(details);
        return response;
    }

    /**
     * 失敗回應（自訂狀態碼 + 訊息）
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    /**
     * 失敗回應（自訂狀態碼 + 訊息 + 詳細錯誤）
     */
    public static <T> ApiResponse<T> error(Integer code, String message, String details) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setDetails(details);
        return response;
    }
}
