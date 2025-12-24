package com.team.playmatebackend.global.config;

import com.team.playmatebackend.domain.user.entity.enums.UserRoleType;
import com.team.playmatebackend.global.Jwt.JwtProvider;
import com.team.playmatebackend.global.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // jwt 필터를 위한 jwtProvider 주입
    private final JwtProvider jwtProvider;



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Security 필터 체인 관리
     *
     * JWT 인증 방식을 사용하기 위한 Security 설정
     * Session을 사용하지 않는 Stateless 구조
     * 기본 인증방식을 비활성화, URL 기반 접근 제어 정책 정의
     *
     * @author 허준형
     * @DateOfCreated 2025-12-23
     * @DateOfEdit 2025-12-23
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtProvider jwtProvider) throws Exception {

        // CSRF 공격은 세션 기반에서 이루어짐
        // JWT는 토큰 기반이라 의미가 크지 않아 Disable
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        // 기존 Form 로그인 Disable, 로그인 필터 직접 구현해야함
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // 사용자명, 비밀번호를 텍스트로 전송, 보안에 취약적이라 Disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // 접근 권한 정책
        http
                .authorizeHttpRequests(auth -> auth
                        // "/login", "/", "/signup" 요청은 모두에게 허용
                        .requestMatchers("/login", "/", "/signup").permitAll()
                        // admin으로 시작하는 모든 요청은 Admin 역할을 가진 사용자만 접근 가능
                        .requestMatchers("/admin/**").hasRole(UserRoleType.ADMIN.name())
                        // 나머지 모든 요청은 로그인 필수
                        .anyRequest().authenticated()
                );


        // JwtFilter 클래스가 Component 등록이 안돼있어서 객체 생성 후 jwtProvider 주입
        JwtFilter jwtFilter = new JwtFilter(jwtProvider);

        // UsernamePasswordAuthenticationFilter 앞에 JwtFilter을 먼저 처리
        // 요청 -> JwtFilter (토큰 확인) -> 이미 인증됨이면 통과 -> 이후 필터 느낌
        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    /**
    * React 백엔드 통신
    * 서로 다른 서버에서 실행중이라면 필요
     *
    * @author 정찬혁
    * @DateOfCreated 2025-12-23
    * @DateOfEdit 2025-12-23
    * */
    //React와 백엔드 통신
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173")  // React 주소
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
