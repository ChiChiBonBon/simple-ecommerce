package com.changbenny.simpleecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Simple E-commerce API")
                        .description("簡易電商系統API文檔\n\n" +
                                "## 如何使用JWT:\n" +
                                "1. 先呼叫 `/auth/register` 或 `/auth/login` 取得 JWT token\n" +
                                "2. 點擊右上角 🔓 **Authorize** 按鈕\n" +
                                "3. 輸入 JWT token (不要加 Bearer 前綴)\n" +
                                "4. 點擊 **Authorize**\n" +
                                "5. 現在可以測試需要驗證的API了！")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ChangBenny")
                                .email("your-email@example.com")))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .description("Enter JWT token without 'Bearer ' prefix")));
    }
}
