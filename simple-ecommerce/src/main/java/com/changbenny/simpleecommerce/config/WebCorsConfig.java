package com.changbenny.simpleecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 配置類。
 * 用於允許瀏覽器端的前端應用程式（例如在不同端口運行的 Live Server）
 * 能夠成功對 Spring Boot 後端 API 發送請求，避免跨來源資源共享錯誤。
 */
@Configuration // 標記這個類是一個配置類，Spring 會在啟動時讀取它。
public class WebCorsConfig {

    /**
     * 註冊一個 WebMvcConfigurer Bean 來客製化 Spring MVC 的配置，包括 CORS。
     * @return WebMvcConfigurer 實例
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * 配置 CORS 映射規則。
             * @param registry CORS 註冊表
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 為所有 API 路徑 ("/**") 設定 CORS 規則
                registry.addMapping("/**")

                        // **設定允許的來源 (Origin)**
                        // "*" 表示允許來自任何網域/端口的請求。
                        // (注意：當使用 allowCredentials(true) 時，此處不能為 "*")
                        .allowedOrigins("*")

                        // **設定允許的 HTTP 方法 (Method)**
                        // "*" 表示允許所有標準 HTTP 方法，包括 GET, POST, PUT, DELETE。
                        // 同時也處理了瀏覽器的 "Preflight" 預檢請求 (OPTIONS)。
                        .allowedMethods("*")

                        // **設定允許的 Header**
                        // "*" 表示允許所有請求 Header，通常包括 Content-Type, Authorization 等。
                        .allowedHeaders("*")

                        // **設定是否允許攜帶憑證 (Credentials)**
                        // 設為 false 表示不允許請求攜帶 Cookie 或 HTTP 認證訊息。
                        // 這與 allowedOrigins("*") 配合使用是標準做法。
                        // 如果您使用 JWT 放在 Header (而非 Cookie) 中，這通常是足夠的。
                        .allowCredentials(false);
            }
        };
    }
}