package com.team.playmatebackend.global.security;

import com.team.playmatebackend.global.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider; // JWT 해석용

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. 토큰 추출 로직을 별도 메서드로 분리하여 호출 (헤더 + 쿼리파라미터 모두 확인)
        String token = resolveToken(request);

        // 2. 토큰이 존재하고, 유효하다면 인증 처리
        if (token != null && jwtProvider.validateToken(token)) {
            String username = jwtProvider.getUsername(token); // 아이디 추출

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                            username, null, List.of());  // 인증 객체 생성

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(auth);            // 로그인 처리
        }

        filterChain.doFilter(request, response); // 다음 필터로
    }

    /**
     * Request에서 토큰을 추출하는 메서드
     * 1순위: Authorization 헤더 (일반 API 요청)
     * 2순위: token 쿼리 파라미터 (SSE 구독 요청)
     */
    private String resolveToken(HttpServletRequest request) {
        // 1. Header 확인
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 이후의 문자열 리턴
        }

        // 2. Query Parameter 확인 (SSE 연결용)
        // 예: /api/notifications/subscribe?token=eyJhb...
        String queryToken = request.getParameter("token");
        if (queryToken != null && !queryToken.isEmpty()) {
            return queryToken;
        }

        return null;
    }
}