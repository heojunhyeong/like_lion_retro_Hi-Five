package com.team.playmatebackend.domain.user.controller;

import com.team.playmatebackend.domain.user.service.UserService;
import com.team.playmatebackend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자 로그아웃 API 추가
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