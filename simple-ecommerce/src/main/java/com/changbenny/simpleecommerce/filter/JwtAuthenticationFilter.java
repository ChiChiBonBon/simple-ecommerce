package com.changbenny.simpleecommerce.filter;

import com.changbenny.simpleecommerce.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String SECRET = "3vN9xWq7TzP4aL6kY2RbU0mJcHsQ8dFe";

    private Key key() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        logger.debug("JWT Filter - Path: {}, Method: {}", path, request.getMethod());

        // 從 Header 取得 JWT Token
        String authHeader = request.getHeader("Authorization");

        // 如果沒有 token，交給 SecurityConfig 決定是否允許訪問
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No JWT token found, skip authentication");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        logger.debug("Token found, validating...");

        // 驗證 JWT Token
        if (!JwtUtil.isValid(token)) {
            logger.warn("Invalid or expired token");
            sendErrorResponse(response, 401, "Invalid or expired token");
            return;
        }

        // 解析 JWT 並將用戶資訊放入 SecurityContext
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String email = claims.get("email", String.class);

            logger.debug("Token validated - userId: {}, email: {}", userId, email);

            // 設定 Spring Security 認證資訊
            var authToken = new UsernamePasswordAuthenticationToken(
                    userId, null, AuthorityUtils.NO_AUTHORITIES);
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // 將用戶資訊放入 request attributes（供 Controller 使用）
            request.setAttribute("userId", Integer.valueOf(userId));
            request.setAttribute("email", email);

        } catch (Exception e) {
            logger.error("Token parsing failed: {}", e.getMessage());
            sendErrorResponse(response, 401, "Invalid token format");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 發送錯誤回應
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"error\":\"%s\"}", message));
    }
}