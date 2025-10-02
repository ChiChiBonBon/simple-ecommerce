package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.dto.*;
import com.changbenny.simpleecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "訂單管理 (Order)", description = "訂單的建立與查詢相關 API")
@RestController
@SecurityRequirement(name = "JWT")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    @Operation(summary = "建立訂單", description = "為指定使用者建立新訂單")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(
            @PathVariable Integer userId,
            @RequestBody @Valid CreateOrderRequestDTO createOrderRequestDTO) {

        Integer orderId = orderService.createOrder(userId, createOrderRequestDTO);
        OrderResponseDTO orderResponseDTO = orderService.getOrderById(orderId);

        // 統一返回 HTTP 200
        return ResponseEntity.ok(ApiResponse.success("訂單建立成功", orderResponseDTO));
    }

    @PostMapping("/users/{userId}/orders/search")
    @Operation(summary = "查詢訂單列表", description = "分頁查詢指定使用者的訂單")
    public ResponseEntity<ApiResponse<PageResponseDTO<OrderResponseDTO>>> getOrders(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "24") @Max(24) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

        // 組裝查詢參數
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        // 取得 order list
        List<OrderResponseDTO> orderResponseDTOS = orderService.getOrders(orderQueryParams);

        // 取得 order 總數
        Integer count = orderService.countOrders(orderQueryParams);

        // 分頁封裝
        PageResponseDTO<OrderResponseDTO> pageResponseDTO = new PageResponseDTO<>();
        pageResponseDTO.setLimit(limit);
        pageResponseDTO.setOffset(offset);
        pageResponseDTO.setTotal(count);
        pageResponseDTO.setResults(orderResponseDTOS);

        // 統一返回 HTTP 200
        return ResponseEntity.ok(ApiResponse.success("查詢成功", pageResponseDTO));
    }
}