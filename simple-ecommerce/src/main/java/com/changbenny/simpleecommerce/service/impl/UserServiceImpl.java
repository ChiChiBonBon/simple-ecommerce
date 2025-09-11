package com.changbenny.simpleecommerce.service.impl;

import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.entity.UserEntity;
import com.changbenny.simpleecommerce.repository.UserRepository;
import com.changbenny.simpleecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    //logger日誌記錄
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public Integer register(UserRegisterRequestDTO userRegisterRequestDTO) {
        //檢查註冊的email
        UserEntity userEntity = userRepository.getUserByEmail(userRegisterRequestDTO.getEmail());

        if(userEntity != null){
            logger.warn("該email {} 已經被註冊",userEntity.getEmail());
            //重覆註冊的email，丟400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //創建帳號
        return userRepository.createUser(userRegisterRequestDTO);
    }

    @Override
    public UserEntity getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }
}
