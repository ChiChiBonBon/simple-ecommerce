package com.changbenny.simpleecommerce.repository;

import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.entity.UserEntity;

public interface UserRepository {
    Integer createUser(UserRegisterRequestDTO userRegisterRequestDTO);

    UserEntity getUserById(Integer userId);

    UserEntity getUserByEmail(String email);

    UserEntity saveUser(UserEntity user);
}
