package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.dto.CreateOrderRequestDTO;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOrderRequestDTO createOrderRequestDTO);
}
