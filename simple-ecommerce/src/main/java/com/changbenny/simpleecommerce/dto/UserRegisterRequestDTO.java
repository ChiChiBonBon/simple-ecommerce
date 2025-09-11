package com.changbenny.simpleecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRegisterRequestDTO {

    @NotBlank//不能是 null、不能是 ""、不能是 " "
    @Email//驗證email
    @JsonProperty("e_mail")//接收request或處理response的指定此json欄位
    private String email;

    @NotBlank//不能是 null、不能是 ""、不能是 " "
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 只接收但不回傳
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
