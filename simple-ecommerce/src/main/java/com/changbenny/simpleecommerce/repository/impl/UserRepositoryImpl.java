package com.changbenny.simpleecommerce.repository.impl;

import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.entity.UserEntity;
import com.changbenny.simpleecommerce.repository.UserRepository;
import com.changbenny.simpleecommerce.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createUser(UserRegisterRequestDTO userRegisterRequestDTO) {
        String sql = " INSERT INTO user(email, password, created_date, last_modified_date) " +
                     " VALUES (:email, :password, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequestDTO.getEmail());
        map.put("password", userRegisterRequestDTO.getPassword());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        //KeyHolder存放資料庫產生的主鍵
        //自動產生的 key 塞到 GeneratedKeyHolder，為KeyHolder的實作類別
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        //取得SQL執行後，自動生成的主鍵
        int userId = keyHolder.getKey().intValue();

        return userId;
    }

    @Override
    public UserEntity getUserById(Integer userId) {
        String sql = " SELECT user_id, email, password, created_date, last_modified_date " +
                     " FROM user WHERE user_id = :userId ";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<UserEntity> userEntityListist = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (userEntityListist.size() > 0) {
            return userEntityListist.get(0);
        } else {
            return null;
        }
    }

    @Override
    public UserEntity getUserByEmail(String email) {
            String sql = " SELECT user_id, email, password, created_date, last_modified_date " +
                         " FROM user WHERE email = :email ";

            Map<String, Object> map = new HashMap<>();
            map.put("email", email);

            List<UserEntity> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

            if (userList.size() > 0) {
                return userList.get(0);
            } else {
                return null;
            }
        }
}
