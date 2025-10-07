package com.changbenny.simpleecommerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // 告訴Spring這是Bean的定義來源
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        //FdaService依賴的Bean
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        //FdaService用來解析JSON的Bean
        return new ObjectMapper();
    }
}
