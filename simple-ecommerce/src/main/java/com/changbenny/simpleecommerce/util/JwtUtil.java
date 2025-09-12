package com.changbenny.simpleecommerce.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // JWT 的簽章密鑰（至少 32 bytes)
    private static final String SECRET = "3vN9xWq7TzP4aL6kY2RbU0mJcHsQ8dFe";

    // Token 的有效時間（這裡設為 1 小時 = 60 分 * 60 秒 * 1000 毫秒）
    private static final long EXP_MS = 60 * 60 * 1000; // 1h

    // 取得簽章用的 Key，使用 HS256 演算法
    private static Key key() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    //產生 JWT Token
    public static String generate(Integer userId, String email){
        Date now = new Date();
        return Jwts.builder()
                // 設定主體，這裡放 userId
                .setSubject(String.valueOf(userId))
                // 自訂欄位，放使用者 email
                .claim("email", email)
                // Token 簽發時間
                .setIssuedAt(now)
                // Token 過期時間（簽發時間 + 有效期）
                .setExpiration(new Date(now.getTime() + EXP_MS))
                // 使用 HS256 演算法與密鑰簽章
                .signWith(key(), SignatureAlgorithm.HS256)
                // 轉成字串格式
                .compact();
    }

    //驗證 JWT Token 是否有效
    public static boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key()) // 設定簽章用的 key
                    .build()
                    .parseClaimsJws(token); // 嘗試解析 token（會同時驗證簽章與過期時間）
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 任一異常都視為驗證失敗
            return false;
        }
    }
}