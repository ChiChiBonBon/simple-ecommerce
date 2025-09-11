package com.changbenny.simpleecommerce.repository;

import com.changbenny.simpleecommerce.entity.OrderItemEntity;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository {
    Integer createOrder(Integer userId, BigDecimal totalAmount);

    void  createOrderItems(Integer orderId, List<OrderItemEntity> orderItemEntityList);
}
