package com.changbenny.simpleecommerce.service.impl;

import com.changbenny.simpleecommerce.dto.UserLoginRequestDTO;
import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.dto.UserResponseDTO;
import com.changbenny.simpleecommerce.entity.UserEntity;
import com.changbenny.simpleecommerce.repository.UserRepository;
import com.changbenny.simpleecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    //logger日誌記錄
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Integer register(UserRegisterRequestDTO userRegisterRequestDTO) {
        //檢查註冊的email
        UserEntity userEntity = userRepository.getUserByEmail(userRegisterRequestDTO.getEmail());

        if(userEntity != null){
            logger.warn("該email {} 已經被註冊",userEntity.getEmail());
            //重覆註冊的email，丟400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //用 BCrypt（Spring Security 內建）
        // 註冊時
        String hashedPassword = passwordEncoder.encode(userRegisterRequestDTO.getPassword());
        userRegisterRequestDTO.setPassword(hashedPassword);

        //創建帳號
        return userRepository.createUser(userRegisterRequestDTO);
    }

    @Override
    public UserResponseDTO getUserById(Integer userId) {
        UserEntity userEntity =  userRepository.getUserById(userId);

        if(userEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"使用者不存在");
        }

        return convertToDTO(userEntity);
    }

    @Override
    public UserResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        UserEntity userEntity = userRepository.getUserByEmail(userLoginRequestDTO.getEmail());

        //檢查user是否存在
        if(userEntity == null){
            logger.warn(" 該email {} 尚未註冊",userLoginRequestDTO.getEmail());
            //尚未註冊的email
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"使用者不存在");
        }

        //比較密碼
        if(passwordEncoder.matches(
                userLoginRequestDTO.getPassword(),  // 使用者輸入的明文
                userEntity.getPassword()            // 資料庫的加密密碼
        )){
            return convertToDTO(userEntity);
        }else{
            logger.warn("登入失敗: email {}",userEntity.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"帳號或密碼錯誤");
        }
    }

    private UserResponseDTO convertToDTO(UserEntity entity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(entity.getUserId());
        dto.setEmail(entity.getEmail());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        return dto;
    }


    //驗證用戶的密碼是否正確
    @Override
    public boolean verifyPassword(Integer userId, String rawPassword) {
        UserEntity userEntity = userRepository.getUserById(userId);

        if (userEntity == null) {
            logger.warn("驗證密碼失敗: userId {} 不存在", userId);
            return false;
        }

        // 使用 BCrypt 驗證密碼
        return passwordEncoder.matches(rawPassword, userEntity.getPassword());
    }

    //更新用戶密碼
    @Override
    public void updatePassword(Integer userId, String newPassword) {
        UserEntity userEntity = userRepository.getUserById(userId);

        if (userEntity == null) {
            logger.warn("更新密碼失敗: userId {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "使用者不存在");
        }

        // 加密新密碼
        String hashedPassword = passwordEncoder.encode(newPassword);
        userEntity.setPassword(hashedPassword);

        // 儲存到資料庫
        userRepository.saveUser(userEntity);

        logger.info("使用者 {} 已成功更新密碼", userEntity.getEmail());
    }
}
