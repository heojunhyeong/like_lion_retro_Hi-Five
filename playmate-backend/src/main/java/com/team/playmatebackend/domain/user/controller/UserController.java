package com.team.playmatebackend.domain.user.controller;

import com.team.playmatebackend.domain.user.dto.LoginRequestDto;
import com.team.playmatebackend.domain.user.dto.LoginResponseDto;
import com.team.playmatebackend.domain.user.dto.UserCreateRequest;  //
import com.team.playmatebackend.domain.user.service.UserService;
import com.team.playmatebackend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // REST API 컨트롤러
@RequiredArgsConstructor // 생성자 자동 생성
@RequestMapping("/api/users") // 기본 URL
public class UserController {
    private final UserService userService;

    /**
     * 로그인 API
     * 로그인 성공 시 Access Token과 Refresh Token 반환
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh Token으로 새 Access Token 발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        try {
            String newAccessToken = userService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).build();
        }
    }

    // 회원가입 api
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Long>> register(@RequestBody UserCreateRequest request) {
        Long userId = userService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success(userId));

    }
}