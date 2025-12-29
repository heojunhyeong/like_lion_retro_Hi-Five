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

        String header = request.getHeader("Authorization"); // 헤더 조회

        // Authorization: Bearer xxx 형태인지 확인
        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);           // Bearer 제거
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
}
