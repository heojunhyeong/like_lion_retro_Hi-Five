package com.team.playmatebackend.global.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // 스프링 빈 등록
public class JwtProvider {

    private static final String SECRET_KEY =
            "playmate-playmate-playmate-playmate-playmate"; // JWT 서명 키

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60; // 토큰 유효시간 (1시간)

    public String createToken(String username) {
        return Jwts.builder()
                .subject(username)                       // 토큰 주인 (아이디)
                .issuedAt(new Date())                    // 발급 시간
                .expiration(new Date(
                        System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(Keys.hmacShaKeyFor(
                        SECRET_KEY.getBytes()))          // 서명
                .compact();                               // JWT 문자열 생성
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

}


