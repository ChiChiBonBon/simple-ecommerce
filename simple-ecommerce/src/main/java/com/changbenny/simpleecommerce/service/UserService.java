package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.entity.UserEntity;
import org.springframework.http.ResponseEntity;

public interface UserService {
    Integer register(UserRegisterRequestDTO userRegisterRequestDTO);

    UserEntity getUserById(Integer userId);
}
