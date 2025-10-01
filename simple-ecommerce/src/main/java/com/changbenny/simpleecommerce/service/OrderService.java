package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.dto.CreateOrderRequestDTO;
import com.changbenny.simpleecommerce.dto.OrderQueryParams;
import com.changbenny.simpleecommerce.dto.OrderResponseDTO;
import com.changbenny.simpleecommerce.entity.OrderEntity;

import java.util.List;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOrderRequestDTO createOrderRequestDTO);

    OrderResponseDTO getOrderById(Integer orderId);

    Integer countOrders(OrderQueryParams orderQueryParams);

    List<OrderResponseDTO> getOrders(OrderQueryParams orderQueryParams);
}
