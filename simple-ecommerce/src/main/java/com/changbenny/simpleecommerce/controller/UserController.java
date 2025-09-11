package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.dto.UserRegisterRequestDTO;
import com.changbenny.simpleecommerce.dto.UserResponseDTO;
import com.changbenny.simpleecommerce.entity.UserEntity;
import com.changbenny.simpleecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("user/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegisterRequestDTO userRegisterRequestDTO) {
        Integer userId= userService.register(userRegisterRequestDTO);

        UserEntity userEntity = userService.getUserById(userId);

        //ResponseDTO
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setEmail(userEntity.getEmail());
        userResponseDTO.setPassword(userEntity.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }
}
