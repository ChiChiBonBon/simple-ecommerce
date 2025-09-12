package com.changbenny.simpleecommerce.dto;

import lombok.Data;

//產生以下
//Getter/Setter
//toString()
//equals() / hashCode()
//建構子
@Data
public class AuthResponseDTO {
    private String email;
    private String accessToken;
    private Long expiresIn; // 秒
}
