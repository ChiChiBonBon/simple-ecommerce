package com.changbenny.simpleecommerce.filter;

import com.changbenny.simpleecommerce.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;

@Component
@Order(1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET = "3vN9xWq7TzP4aL6kY2RbU0mJcHsQ8dFe";

    private Key key() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("========== JWT Filter Debug ==========");
        System.out.println("Path: " + path);
        System.out.println("Method: " + method);

        // 允許通過的路徑（不需要JWT驗證）
        if (isPublicPath(path, method)) {
            System.out.println("Public path, skip JWT validation");
            filterChain.doFilter(request, response);
            return;
        }

        // 從Header取得JWT Token
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Missing or invalid Authorization header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            response.setContentType("application/json");
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("Token (first 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");

        // 驗證JWT Token
        boolean isValid = JwtUtil.isValid(token);
        System.out.println("Token valid: " + isValid);

        if (!isValid) {
            System.out.println("Token validation failed");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
            response.setContentType("application/json");
            return;
        }

        // 解析JWT並將用戶資訊放入request attribute
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String email = claims.get("email", String.class);

            System.out.println("Token parsed successfully - userId: " + userId + ", email: " + email);

            // 關鍵：用「三參數」建構子，authenticated=true（即使沒有角色）
            var authToken = new UsernamePasswordAuthenticationToken(
                    userId, null, AuthorityUtils.NO_AUTHORITIES);
            SecurityContextHolder.getContext().setAuthentication(authToken);

            request.setAttribute("userId", Integer.valueOf(userId));
            request.setAttribute("email", email);

        } catch (Exception e) {
            System.out.println("Token parsing exception: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Invalid token format\"}");
            response.setContentType("application/json");
            return;
        }

        System.out.println("========== JWT Filter End ==========");
        filterChain.doFilter(request, response);
    }

    /**
     * 判斷是否為公開路徑（不需要JWT驗證）
     */
    private boolean isPublicPath(String path, String method) {
        // 允許所有OPTIONS請求（CORS預檢請求）
        if ("OPTIONS".equals(method)) {
            return true;
        }

        // Swagger相關路徑
        if (path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/webjars/") ||
                path.equals("/auth/login") ||
                path.equals("/auth/register")
        ) {
            return true;
        }

        // 健康檢查
        if (path.startsWith("/actuator")) {
            return true;
        }

        // 認證相關API（註冊、登入）
        if (path.equals("/auth/register") ||
                path.equals("/auth/login") ||
                path.equals("/users/register") ||
                path.equals("/users/login")) {
            return true;
        }

        // 公開的商品查詢API（可選，看你的需求）
        if (path.equals("/products") && "POST".equals(method)) {
            return true; // 允許不登入也能查詢商品
        }

        if (path.startsWith("/products/search/") && "POST".equals(method)) {
            return true; // 允許不登入也能查詢單一商品
        }

        return false;
    }
}
