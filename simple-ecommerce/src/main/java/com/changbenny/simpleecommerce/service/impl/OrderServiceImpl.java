package com.changbenny.simpleecommerce.service.impl;

import com.changbenny.simpleecommerce.dto.*;
import com.changbenny.simpleecommerce.entity.OrderEntity;
import com.changbenny.simpleecommerce.entity.OrderItemEntity;
import com.changbenny.simpleecommerce.entity.ProductEntity;
import com.changbenny.simpleecommerce.entity.UserEntity;
import com.changbenny.simpleecommerce.repository.OrderRepository;
import com.changbenny.simpleecommerce.service.OrderService;
import com.changbenny.simpleecommerce.service.ProductService;
import com.changbenny.simpleecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

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
        UserResponseDTO userResponseDTO = userService.getUserById(userId);

        if (userResponseDTO == null) {
            //userId不存在，回傳400
            log.warn("該userId {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //初始化總價錢
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();

        for(BuyItemDTO buyItemDTO : createOrderRequestDTO.getBuyItemDTOList()){
            ProductResponseDTO productResponseDTO = productService.getProductById(buyItemDTO.getProductId());

            // 檢查 product 是否存在、庫存是否足夠
            if (productResponseDTO == null) {
                //商品不存在，回傳400
                log.warn("商品ID {} 不存在", buyItemDTO.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (productResponseDTO.getStock() < buyItemDTO.getQuantity()) {
                //商品庫存不足，回傳400
                log.warn("商品ID {} 庫存數量不足，無法購買。剩餘庫存 {}，欲購買數量 {}",
                        buyItemDTO.getProductId(), productResponseDTO.getStock(), buyItemDTO.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 扣除商品庫存
            productService.updateStock(productResponseDTO.getProductId(),
                    productResponseDTO.getStock() - buyItemDTO.getQuantity());

            //計算總價錢
            BigDecimal amount = productResponseDTO.getPrice()
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
    public OrderResponseDTO getOrderById(Integer orderId) {
        OrderEntity orderEntity = orderRepository.getOrderById(orderId);

        List<OrderItemEntity> orderItemEntityList = orderRepository.getOrderItemsByOrderId(orderId);

        orderEntity.setOrderItemEntityList(orderItemEntityList);

        return convertToDTO(orderEntity);
    }

    @Override
    public Integer countOrders(OrderQueryParams orderQueryParams) {
        return orderRepository.countOrders(orderQueryParams);
    }

    @Override
    public List<OrderResponseDTO> getOrders(OrderQueryParams orderQueryParams) {
        // 根據查詢參數，先撈出所有符合條件的訂單（只包含訂單主檔，不含訂單中的訂單項目清單）
        List<OrderEntity> orderList = orderRepository.getOrders(orderQueryParams);

        // 對每一張訂單，額外查詢它的訂單項目清單
        for (OrderEntity order : orderList) {
            // 根據訂單ID查詢所有的訂單項目清單
            List<OrderItemEntity> orderItemEntityList = orderRepository.getOrderItemsByOrderId(order.getOrderId());
            // 將查到的訂單項目清單，塞進對應的訂單物件裡
            order.setOrderItemEntityList(orderItemEntityList);
        }

        // 回傳訂單清單
        return convertToDTOList(orderList);
    }

    public OrderResponseDTO convertToDTO(OrderEntity orderEntity) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(orderEntity.getOrderId());
        orderResponseDTO.setUserId(orderEntity.getUserId());
        orderResponseDTO.setTotalAmount(orderEntity.getTotalAmount());
        orderResponseDTO.setCreatedDate(orderEntity.getCreatedDate());
        orderResponseDTO.setLastModifiedDate(orderEntity.getLastModifiedDate());

        // 轉換 OrderItem List
        if (orderEntity.getOrderItemEntityList() != null) {
            List<OrderItemDTO> orderItemDTOList = orderEntity.getOrderItemEntityList().stream()
                    .map(this::convertOrderItemToDTO)
                    .collect(Collectors.toList());
            orderResponseDTO.setOrderItemList(orderItemDTOList);
        }

        return orderResponseDTO;
    }

    private OrderItemDTO convertOrderItemToDTO(OrderItemEntity entity) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setOrderItemId(entity.getOrderItemId());
        orderItemDTO.setProductId(entity.getProductId());
        orderItemDTO.setProductName(entity.getProductName());
        orderItemDTO.setImageUrl(entity.getImageUrl());
        orderItemDTO.setQuantity(entity.getQuantity());
        orderItemDTO.setAmount(entity.getAmount());
        return orderItemDTO;
    }

    private List<OrderResponseDTO> convertToDTOList(List<OrderEntity> orderEntityList) {
        return orderEntityList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
