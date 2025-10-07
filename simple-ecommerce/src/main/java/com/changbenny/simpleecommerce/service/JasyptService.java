package com.changbenny.simpleecommerce.service;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JasyptService {
    private final StringEncryptor encryptor;

    public JasyptService(@Qualifier("lazyJasyptStringEncryptor") StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    public String encrypt(String plainString) {
        String encryptString = encryptor.encrypt(plainString);
        return encryptString;
    }

    public String decrypt(String encryptedString) {
        String decryptString = encryptor.decrypt(encryptedString);
        return decryptString;
    }
}
