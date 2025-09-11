package com.changbenny.simpleecommerce.rowmapper;

import com.changbenny.simpleecommerce.entity.OrderEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<OrderEntity> {
    @Override
    public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(rs.getInt("order_id"));
        orderEntity.setUserId(rs.getInt("user_id"));
        orderEntity.setTotalAmount(rs.getBigDecimal("total_amount"));
        orderEntity.setCreatedDate(rs.getTimestamp("created_date"));
        orderEntity.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        return orderEntity;
    }
}
