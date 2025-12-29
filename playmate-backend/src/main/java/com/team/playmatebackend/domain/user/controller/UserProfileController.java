package com.team.playmatebackend.domain.user.controller;

import com.team.playmatebackend.domain.user.dto.UserProfileResponseDto;
import com.team.playmatebackend.domain.user.dto.UserProfileUpdateRequestDto;
import com.team.playmatebackend.domain.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

/**
 * 사용자 프로필 관련 요청을 처리하는 컨트롤러
 *
 * JWT 인증을 기반으로 로그인한 사용자의 프로필 정보를 조회한다.
 * 사용자 식별은 Authentication 객체를 통해 수행한다.
 *
 * @author 전진
 * @DateOfCreated 2025-12-27
 * @DateOfEdit 2025-12-29
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    /**
     * 로그인한 사용자의 프로필 정보를 조회한다.
     *
     * JWT 필터를 통해 SecurityContext에 저장된 Authentication 객체에서
     * userId를 추출하여 서비스로 전달한다.
     *
     * @author 전진
     * @DateOfCreated 2025-12-27
     * @DateOfEdit 2025-12-27
     * @param authentication 현재 로그인한 사용자 인증 정보
     * @return 사용자 프로필 응답 DTO
     */
    @GetMapping("/me")
    public UserProfileResponseDto getMyProfile(Authentication authentication) {
        String userId = authentication.getName(); // JWT subject
        return userProfileService.getMyProfile(userId);
    }
    /**
     * 로그인한 사용자의 프로필 정보를 수정한다.
     *
     * Authentication 객체에서 userId를 추출한 뒤,
     * 해당 사용자의 프로필 수정 요청을 서비스로 전달한다.
     *
     * @param authentication 현재 로그인한 사용자 인증 정보
     * @param request 사용자 프로필 수정 요청 DTO
     * @author 전진
     * @DateOfCreated 2025-12-29
     * @DateOfEdit 2025-12-29
     */
    @PutMapping("/me")
    public void updateProfile(
            Authentication authentication,
            @RequestBody UserProfileUpdateRequestDto request
    ) {
        String userId = authentication.getName();
        userProfileService.updateProfile(userId, request);
    }
}
