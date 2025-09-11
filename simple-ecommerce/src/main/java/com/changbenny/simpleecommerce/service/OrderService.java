package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.dto.CreateOrderRequestDTO;
import com.changbenny.simpleecommerce.entity.OrderEntity;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOrderRequestDTO createOrderRequestDTO);

    OrderEntity getOrderById(Integer orderId);
}
