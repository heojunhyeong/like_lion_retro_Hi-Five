package com.team.playmatebackend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Security 전반적인 필터 관리
     * !!!!작성중!!!
     *
     * @author 허준형
     * @DateOfCreated 2025-12-23
     * @DateOfEdit 2025-12-23
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 공격은 세션 기반에서 이루어짐
        // JWT는 토큰 기반이라 의미가 크지 않아 Disable
        http
                .csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }
}
