package com.changbenny.simpleecommerce.config;

import com.changbenny.simpleecommerce.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//CORS 相關 import
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //集中管理 CORS 設定（本地開發 5174/5175）
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // 允許的前端來源（依你的實際 dev 埠補齊）
        cfg.setAllowedOrigins(List.of(
                "http://localhost:5174", "http://127.0.0.1:5174",
                "http://localhost:5175", "http://127.0.0.1:5175"
        ));
        // 允許的方法（含 OPTIONS 供 preflight）
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // 允許的標頭（如使用 JWT，需允許 Authorization）
        cfg.setAllowedHeaders(List.of("*"));
        // 回應可暴露的標頭
        cfg.setExposedHeaders(List.of("Authorization"));
        // 若需要 cookie（通常 JWT 不需要）就設為 true
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    //定義整個系統的安全策略
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //0. 開啟 CORS，讓瀏覽器的 preflight (OPTIONS) 能通過
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 1. 關閉CSRF，因為是Restful API，不用傳統session-cookie
                .csrf(csrf -> csrf.disable())

                // 2.設定 Session 策略為 STATELESS（完全無狀態，交由 JWT 驗證）
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3.設定授權規則
                .authorizeHttpRequests(auth -> auth
                        //3.0允許瀏覽器的 Preflight（OPTIONS）請求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 3.1 認證相關 API（註冊、登入）→ 無需攔截
                        // 放行認證端點
                        .requestMatchers("/auth/register", "/auth/login", "/auth/forgot-password", "/auth/reset-password", "/auth/change-password").permitAll()
                        .requestMatchers("/users/register", "/users/login").permitAll()

                        // 3.2 Swagger 文件相關 → 無需攔截
                        // 放行 Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // 3.3 商品查詢相關 → 可公開訪問
                        // 放行公開的商品查詢
                        .requestMatchers("/products/**").permitAll()

                        // 3.4 外部API相關 → 可公開訪問
                        // 食藥署新聞（公開）
                        .requestMatchers("/fda/news").permitAll()

                        // 3.5 Jasypt 加解密 → 可公開訪問
                        .requestMatchers("/jasypt/**").permitAll()

                        // 其他都需要驗證
                        .anyRequest().authenticated()
                )

                // 4.在UsernamePasswordAuthenticationFilter之前，插入自訂的JWT過濾器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //        // 5.回傳SecurityFilterChain給Spring Security管理
        return http.build();
    }
}
