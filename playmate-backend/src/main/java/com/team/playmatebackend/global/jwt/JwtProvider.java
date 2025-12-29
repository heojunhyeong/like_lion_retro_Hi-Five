package com.team.playmatebackend.global.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // 스프링 빈 등록
public class JwtProvider {

    private static final String SECRET_KEY =
            "playmate-playmate-playmate-playmate-playmate"; // JWT 서명 키

    // Access Token: 1시간 (짧은 유효시간)
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60;
    
    // Refresh Token: 7일 (긴 유효시간)
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;

    // Access Token 생성
    public String createAccessToken(String username) {
        return Jwts.builder()
                .subject(username)                       // 토큰 주인 (아이디)
                .issuedAt(new Date())                    // 발급 시간
                .expiration(new Date(
                        System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME)) // 만료 시간
                .signWith(Keys.hmacShaKeyFor(
                        SECRET_KEY.getBytes()))          // 서명
                .compact();                               // JWT 문자열 생성
    }

    // Refresh Token 생성
    public String createRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(
                        SECRET_KEY.getBytes()))          // 서명 검증
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();                           // username 추출
    }

    // 기존 createToken 메서드는 createAccessToken으로 대체되지만 호환성을 위해 유지
    @Deprecated
    public String createToken(String username) {
        return createAccessToken(username);
    }

}


