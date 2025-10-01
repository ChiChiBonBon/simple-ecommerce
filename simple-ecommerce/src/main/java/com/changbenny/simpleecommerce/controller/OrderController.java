package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.dto.CreateOrderRequestDTO;
import com.changbenny.simpleecommerce.dto.OrderQueryParams;
import com.changbenny.simpleecommerce.dto.OrderResponseDTO;
import com.changbenny.simpleecommerce.dto.PageResponseDTO;
import com.changbenny.simpleecommerce.entity.OrderEntity;
import com.changbenny.simpleecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "JWT")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //建立使用者訂單
    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<OrderResponseDTO> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequestDTO createOrderRequestDTO) {

        System.out.println("========== Controller: createOrder ==========");
        System.out.println("Received userId: " + userId);
        System.out.println("Received request: " + createOrderRequestDTO);

        Integer orderId = orderService.createOrder(userId,createOrderRequestDTO);

        OrderResponseDTO orderResponseDTO = orderService.getOrderById(orderId);

        //回傳201狀態碼，後端已成功建立一個新資源
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }

    //查詢訂單
    @PostMapping("users/{userId}/orders/search")
    public ResponseEntity<PageResponseDTO> getOrders(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "24") @Max(24) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {
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

        return ResponseEntity.status(HttpStatus.OK).body(pageResponseDTO);
    }
}
