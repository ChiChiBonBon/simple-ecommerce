package com.changbenny.simpleecommerce.controller;

import com.changbenny.simpleecommerce.service.JasyptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 加解密控制器
@RestController
@RequestMapping("/jasypt")
@Tag(name = "Jasypt 加解密 API", description = "提供字串的加密與解密功能")
public class JasyptController {

    private final static Logger log = LoggerFactory.getLogger(JasyptController.class);

    @Autowired
    private JasyptService jasyptService;

    @PostMapping("/decrypt")
    @Operation(summary = "解密字串", description = "輸入已加密的字串，回傳解密後的明文。")
    public String decryptString(@RequestParam String encryptedString) {
        log.info("取得 encryptedString: {}", encryptedString);
        return jasyptService.decrypt(encryptedString);
    }

    @PostMapping("/encrypt")
    @Operation(summary = "加密字串", description = "輸入明文字串，回傳加密後的密文。")
    public String encryptString(@RequestParam String plainString) {
        log.info("取得 plainString: {}", plainString);
        return jasyptService.encrypt(plainString);
    }
}
