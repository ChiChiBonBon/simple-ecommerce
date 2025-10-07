package com.changbenny.simpleecommerce.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {
    private final static Logger loger = LoggerFactory.getLogger(JasyptConfig.class);

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {

        // 先從 JVM 參數，沒有再從環境變數取金鑰
        String key = System.getProperty("jasypt.encryptor.password");
        if (key == null || key.isBlank()) {
            key = System.getenv("JASYPT_ENCRYPTOR_PASSWORD");
        }
        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "Missing Jasypt key. Set -Djasypt.encryptor.password=... or env JASYPT_ENCRYPTOR_PASSWORD");
        }

//        StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
//        enc.setAlgorithm("PBEWithHmacSHA512AndAES_256");
//        enc.setPassword(key);

        SimpleStringPBEConfig cfg = new SimpleStringPBEConfig();
        cfg.setPassword(key);
        cfg.setAlgorithm("PBEWithHmacSHA256AndAES_256"); // 目標演算法
        cfg.setKeyObtentionIterations("10000");          // 迭代次數(>=1k，建議1w+)
        cfg.setPoolSize("1");                            // 線程池大小
        cfg.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // Salt
        cfg.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");       // IV（AES 必備）
        cfg.setStringOutputType("base64");               // 密文輸出型別

        PooledPBEStringEncryptor enc = new PooledPBEStringEncryptor();
        enc.setConfig(cfg);
        loger.info("加密後的字串:{}",enc.encrypt(key));
        return enc;
    }
}