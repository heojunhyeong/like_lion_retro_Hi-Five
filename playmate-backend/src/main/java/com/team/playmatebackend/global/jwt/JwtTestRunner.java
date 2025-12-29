package com.team.playmatebackend.global.jwt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 로컬 환경에서 JWT 테스트 토큰 생성을 위한 실행 클래스
 *
 * Spring Boot 애플리케이션이 기동된 이후 자동으로 실행되며,
 * JwtProvider를 이용해 테스트용 JWT 토큰을 생성한다.
 *
 * 생성된 토큰은 콘솔에 출력되며,
 * API 테스트(Http 파일, Postman 등)에서 Authorization 헤더에 사용된다.
 *
 * local 프로파일에서만 동작하며,
 * 운영 환경에서는 실행되지 않는다.
 *
 * @author 전진
 * @DateOfCreated 2025-12-27
 * @DateOfEdit 2025-12-27
 */
@Component
@Profile("local")
public class JwtTestRunner implements CommandLineRunner {

    private final JwtProvider jwtProvider;

    /**
     * JwtProvider를 주입받는 생성자
     *
     * JWT 토큰 생성 로직을 담당하는 컴포넌트를 주입받는다.
     *
     * @author 전진
     * @DateOfCreated 2025-12-27
     * @DateOfEdit 2025-12-27
     * @param jwtProvider JWT 생성 기능을 제공하는 클래스
     */
    public JwtTestRunner(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    /**
     * 애플리케이션 기동 완료 후 자동으로 실행되는 메서드
     *
     * 테스트용 사용자 식별자(userId)를 기준으로 JWT 토큰을 생성하고,
     * 생성된 토큰을 콘솔에 출력한다.
     *
     * 해당 토큰은 인증이 필요한 API 테스트에 사용된다.
     *
     * @author 전진
     * @DateOfCreated 2025-12-27
     * @DateOfEdit 2025-12-27
     * @param args 애플리케이션 실행 시 전달되는 인자
     */
    @Override
    public void run(String... args) {
        String token = jwtProvider.createToken("jin");

        System.out.println("=================================");
        System.out.println("테스트용 JWT 토큰:");
        System.out.println(token);
        System.out.println("=================================");
    }
}
