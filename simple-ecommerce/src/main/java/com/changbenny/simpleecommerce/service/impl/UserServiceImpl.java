package com.changbenny.simpleecommerce.service.impl;

import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.entity.UserEntity;
import com.changbenny.simpleecommerce.repository.UserRepository;
import com.changbenny.simpleecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Integer register(UserRegisterRequestDTO userRegisterRequestDTO) {
        return userRepository.createUser(userRegisterRequestDTO);
    }

    @Override
    public UserEntity getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }

}
