package com.changbenny.simpleecommerce.config;

import com.changbenny.simpleecommerce.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 的主要設定類
 * - 定義哪些 API 需要認證
 * - 加入自訂的 JWT 驗證過濾器
 * - 關閉session，讓系統完全基於Token驗證
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 建構式注入自訂的 JWT 過濾器
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //定義整個系統的安全策略
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 關閉CSRF，因為是Restful API，不用傳統session-cookie
                .csrf(csrf -> csrf.disable())

                // 2.設定 Session 策略為 STATELESS（完全無狀態，交由 JWT 驗證）
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3.設定授權規則
                .authorizeHttpRequests(auth -> auth
                        // 3.1 認證相關 API（註冊、登入）→ 無需攔截
                        // 放行認證端點
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/users/register", "/users/login").permitAll()

                        // 3.2 Swagger 文件相關 → 無需攔截
                        // 放行 Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // 3.3 商品查詢相關 → 可公開訪問
                        // 放行公開的商品查詢
                        .requestMatchers("/products/**").permitAll()

                        // 其他都需要驗證
                        .anyRequest().authenticated()
                )

                // 4.在UsernamePasswordAuthenticationFilter之前，插入自訂的JWT過濾器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 5.回傳SecurityFilterChain給Spring Security管理
        return http.build();
    }
}
