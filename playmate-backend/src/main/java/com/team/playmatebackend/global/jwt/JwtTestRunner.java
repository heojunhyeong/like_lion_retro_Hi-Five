package com.team.playmatebackend.global.jwt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 로컬 환경에서 JWT 테스트 토큰 생성을 위한 실행 클래스
 *
 * @author 전진
 * @DateOfCreated 2025-12-27
 * @DateOfEdit 2025-12-30
 */
@Component
@Profile("local")
public class JwtTestRunner implements CommandLineRunner {

    private final JwtProvider jwtProvider;

    public JwtTestRunner(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void run(String... args) {
        String token = jwtProvider.createAccessToken("jin");

        Date now = new Date();
        Date exp = jwtProvider.getExpiration(token);

        System.out.println("=================================");
        System.out.println("테스트용 JWT 토큰:");
        System.out.println(token);
        System.out.println();
        System.out.println("현재 서버 시간(now): " + now);
        System.out.println("토큰 만료 시간(exp): " + exp);
        System.out.println("남은 시간(ms): " + (exp.getTime() - now.getTime()));
        System.out.println("=================================");
    }
}
