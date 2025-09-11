package com.changbenny.simpleecommerce.repository.impl;

import com.changbenny.simpleecommerce.entity.OrderEntity;
import com.changbenny.simpleecommerce.entity.OrderItemEntity;
import com.changbenny.simpleecommerce.repository.OrderRepository;
import com.changbenny.simpleecommerce.rowmapper.OrderItemRowMapper;
import com.changbenny.simpleecommerce.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createOrder(Integer userId, BigDecimal totalAmount) {
        String sqlString = " INSERT INTO `order` (user_id, total_amount, created_date, last_modified_date) " +
                           " VALUES (:userId, :totalAmount, :createdDate, :lastModifiedDate) ";

        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("userId", userId);
        orderMap.put("totalAmount", totalAmount);

        Date now = new Date();
        orderMap.put("createdDate", now);
        orderMap.put("lastModifiedDate", now);

        //KeyHolder存放資料庫產生的主鍵
        //自動產生的 key 塞到 GeneratedKeyHolder，為KeyHolder的實作類別
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sqlString, new MapSqlParameterSource(orderMap), keyHolder);

        //取得SQL執行後，自動生成的主鍵
        int orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItemEntity> orderItemEntityList) {
        // 使用for-loop處理orderItemEntityList
        for (OrderItemEntity orderItemEntity : orderItemEntityList) {
            String sql = " INSERT INTO order_item(order_id, product_id, quantity, amount) " +
                         " VALUES (:orderId, :productId, :quantity, :amount) ";

            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("productId", orderItemEntity.getProductId());
            map.put("quantity", orderItemEntity.getQuantity());
            map.put("amount", orderItemEntity.getAmount());

            namedParameterJdbcTemplate.update(sql, map);
        }
    }

    @Override
    public OrderEntity getOrderById(Integer orderId) {
        String sqlString = " SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                           " FROM `order` WHERE order_id = :orderId ";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<OrderEntity> orderList = namedParameterJdbcTemplate.query(sqlString, map, new OrderRowMapper());

        if (orderList.size() > 0) {
            return orderList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OrderItemEntity> getOrderItemsByOrderId(Integer orderId) {
        String sql = " SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url " +
                     " FROM order_item as oi " +
                     " LEFT JOIN product as p ON oi.product_id = p.product_id " +
                     " WHERE oi.order_id = :orderId ";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        //訂單中的訂單項目清單
        List<OrderItemEntity> orderItemEntityList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        return orderItemEntityList;
    }


}
