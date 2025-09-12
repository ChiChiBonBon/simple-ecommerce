package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.dto.CreateOrderRequestDTO;
import com.changbenny.simpleecommerce.dto.OrderQueryParams;
import com.changbenny.simpleecommerce.entity.OrderEntity;

import java.util.List;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOrderRequestDTO createOrderRequestDTO);

    OrderEntity getOrderById(Integer orderId);

    Integer countOrders(OrderQueryParams orderQueryParams);

    List<OrderEntity> getOrders(OrderQueryParams orderQueryParams);
}
