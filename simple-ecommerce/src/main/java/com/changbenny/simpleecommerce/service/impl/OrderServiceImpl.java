package com.changbenny.simpleecommerce.service.impl;

import com.changbenny.simpleecommerce.dto.BuyItemDTO;
import com.changbenny.simpleecommerce.dto.CreateOrderRequestDTO;
import com.changbenny.simpleecommerce.entity.OrderEntity;
import com.changbenny.simpleecommerce.entity.OrderItemEntity;
import com.changbenny.simpleecommerce.entity.ProductEntity;
import com.changbenny.simpleecommerce.entity.UserEntity;
import com.changbenny.simpleecommerce.repository.OrderRepository;
import com.changbenny.simpleecommerce.service.OrderService;
import com.changbenny.simpleecommerce.service.ProductService;
import com.changbenny.simpleecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    //交易控制，交易失敗rollback
    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequestDTO createOrderRequestDTO) {
        //檢查user是否存在
        UserEntity userEntity = userService.getUserById(userId);

        if (userEntity == null) {
            //userId不存在，回傳400
            log.warn("該userId {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //初始化總價錢
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();

        for(BuyItemDTO buyItemDTO : createOrderRequestDTO.getBuyItemDTOList()){
            ProductEntity productEntity = productService.getProductById(buyItemDTO.getProductId());

            // 檢查 product 是否存在、庫存是否足夠
            if (productEntity == null) {
                //商品不存在，回傳400
                log.warn("商品ID {} 不存在", buyItemDTO.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (productEntity.getStock() < buyItemDTO.getQuantity()) {
                //商品庫存不足，回傳400
                log.warn("商品ID {} 庫存數量不足，無法購買。剩餘庫存 {}，欲購買數量 {}",
                        buyItemDTO.getProductId(), productEntity.getStock(), buyItemDTO.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 扣除商品庫存
            productService.updateStock(productEntity.getProductId(),
                    productEntity.getStock() - buyItemDTO.getQuantity());

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

    @Override
    public OrderEntity getOrderById(Integer orderId) {
        OrderEntity orderEntity = orderRepository.getOrderById(orderId);

        List<OrderItemEntity> orderItemEntityList = orderRepository.getOrderItemsByOrderId(orderId);

        orderEntity.setOrderItemEntityList(orderItemEntityList);

        return orderEntity;
    }
}
