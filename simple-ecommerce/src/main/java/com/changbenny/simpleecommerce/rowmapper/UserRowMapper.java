package com.changbenny.simpleecommerce.rowmapper;

import com.changbenny.simpleecommerce.entity.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserEntity> {
    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(rs.getInt("user_id"));
        userEntity.setEmail(rs.getString("email"));
        userEntity.setPassword(rs.getString("password"));
        userEntity.setCreatedDate(rs.getTimestamp("created_date"));
        userEntity.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        return userEntity;
    }
}
