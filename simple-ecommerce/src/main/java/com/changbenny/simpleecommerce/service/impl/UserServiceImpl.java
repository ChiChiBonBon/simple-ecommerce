package com.changbenny.simpleecommerce.service.impl;

import com.changbenny.simpleecommerce.dto.UserLoginRequestDTO;
import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.entity.UserEntity;
import com.changbenny.simpleecommerce.repository.UserRepository;
import com.changbenny.simpleecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
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
        //使用MD5生成密碼的雜湊值，並回傳成16進位字串
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequestDTO.getPassword().getBytes());
        userRegisterRequestDTO.setPassword(hashedPassword);

        //創建帳號
        return userRepository.createUser(userRegisterRequestDTO);
    }

    @Override
    public UserEntity getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public UserEntity login(UserLoginRequestDTO userLoginRequestDTO) {
        UserEntity userEntity = userRepository.getUserByEmail(userLoginRequestDTO.getEmail());

        //檢查user是否存在
        if(userEntity == null){
            logger.warn(" 該email {} 尚未註冊",userLoginRequestDTO.getEmail());
            //尚未註冊的email，丟400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用MD5生成密碼的雜湊值，並回傳成16進位字串
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequestDTO.getPassword().getBytes());

        //比較密碼
        if(userEntity.getPassword().equals(hashedPassword)){
            return userEntity;
        }else{
            logger.warn("email {} 的密碼不正確",userEntity.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
