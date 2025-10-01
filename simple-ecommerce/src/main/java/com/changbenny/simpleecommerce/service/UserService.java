package com.changbenny.simpleecommerce.service;

import com.changbenny.simpleecommerce.dto.UserLoginRequestDTO;
import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.dto.UserResponseDTO;
import com.changbenny.simpleecommerce.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    Integer register(UserRegisterRequestDTO userRegisterRequestDTO);

    UserResponseDTO getUserById(Integer userId);

    UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);
}
