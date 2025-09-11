package com.changbenny.simpleecommerce.rowmapper;

import com.changbenny.simpleecommerce.entity.OrderItemEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItemEntity> {
    @Override
    public OrderItemEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        //從資料表order_item撈出欄位值，設定到Entity屬性
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderItemId(rs.getInt("order_item_id")); //PK
        orderItemEntity.setOrderId(rs.getInt("order_id"));//FK
        orderItemEntity.setProductId(rs.getInt("product_id"));//FK
        orderItemEntity.setQuantity(rs.getInt("quantity"));//商品數量
        orderItemEntity.setAmount(rs.getBigDecimal("amount"));//小計金額

        // 從join product表多撈的欄位（非order_item表本身）
        orderItemEntity.setProductName(rs.getString("product_name"));//商品名稱
        orderItemEntity.setImageUrl(rs.getString("image_url"));//商品圖片url

        return orderItemEntity;
    }
}
