package com.team.playmatebackend.domain.user.controller;

import com.team.playmatebackend.domain.user.dto.LoginRequestDto;
import com.team.playmatebackend.domain.user.service.UserService;
import com.team.playmatebackend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // REST API 컨트롤러
@RequiredArgsConstructor // 생성자 자동 생성
@RequestMapping("/api/users") // 기본 URL
public class UserController {

    private final UserService userService;

    /**
     * 로그인 API
     * - 로그인 성공 시 JWT 토큰 반환
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequestDto request
    ) {
        String token = userService.login(request); // 로그인 + JWT 발급
        return ResponseEntity.ok(token);
    }

    /**
     * 로그아웃 API
     *
     * @author 김지번
     * @DateOfCreated 2025-12-23
     * @DateOfEdit 2025-12-23
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(Authentication authentication) {
        String userId = authentication.getName(); // SecurityContext에서 userId 추출
        userService.logout(userId);
        return ApiResponse.success();
    }
}