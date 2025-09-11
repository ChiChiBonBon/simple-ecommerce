package com.changbenny.simpleecommerce.service.impl;

import com.changbenny.simpleecommerce.dto.BuyItemDTO;
import com.changbenny.simpleecommerce.dto.CreateOrderRequestDTO;
import com.changbenny.simpleecommerce.entity.OrderItemEntity;
import com.changbenny.simpleecommerce.entity.ProductEntity;
import com.changbenny.simpleecommerce.repository.OrderRepository;
import com.changbenny.simpleecommerce.service.OrderService;
import com.changbenny.simpleecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    //交易控制，交易失敗rollback
    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequestDTO createOrderRequestDTO) {
        //初始化總價錢
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();

        for(BuyItemDTO buyItemDTO : createOrderRequestDTO.getBuyItemDTOList()){
            ProductEntity productEntity = productService.getProductById(buyItemDTO.getProductId());

            //計算總價錢
            BigDecimal amount = productEntity.getPrice()
                                             .multiply(BigDecimal.valueOf(buyItemDTO.getQuantity()));
            //BigDecimal型別相加
            totalAmount = totalAmount.add(amount);

            //轉換BuyItemDTO to OrderItemEntity
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setProductId(buyItemDTO.getProductId());
            orderItemEntity.setQuantity(buyItemDTO.getQuantity());
            orderItemEntity.setAmount(amount);

            orderItemEntityList.add(orderItemEntity);
        }

        //建立訂單
        Integer orderId = orderRepository.createOrder(userId,totalAmount);

        //建立訂單項目
        orderRepository.createOrderItems(orderId,orderItemEntityList);

        return orderId;
    }
}
