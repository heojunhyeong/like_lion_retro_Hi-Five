package com.team.playmatebackend.domain.user.controller;

import com.team.playmatebackend.domain.user.dto.PasswordChangeRequestDto;
import com.team.playmatebackend.domain.user.dto.PasswordVerifyRequestDto;
import com.team.playmatebackend.domain.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/password")
public class UserPasswordController {

    private final UserProfileService userProfileService;

    /**
     * 로그인한 사용자의 현재 비밀번호가 일치하는지 검증한다.
     *
     * 비밀번호 변경 전, 사용자가 입력한 현재 비밀번호가
     * 실제 저장된 비밀번호와 일치하는지 확인하기 위한 API이다.
     *
     * 비밀번호가 일치하지 않을 경우 예외를 발생시킨다.
     *
     * @param authentication 현재 로그인한 사용자 인증 정보
     * @param request 비밀번호 검증 요청 DTO
     * @return HTTP 200 OK
     * @author 전진
     * @DateOfCreated 2025-12-30
     * @DateOfEdit 2025-12-30
     */
    @PostMapping("/verify")
    public ResponseEntity<Void> verifyPassword(
            Authentication authentication,
            @RequestBody PasswordVerifyRequestDto request
    ) {
        String userId = authentication.getName();

        userProfileService.verifyPassword(userId, request.getPassword());
        return ResponseEntity.ok().build();
    }


    /**
     * 로그인한 사용자의 비밀번호를 변경한다.
     *
     * 현재 비밀번호가 일치하는 경우에만
     * 새 비밀번호로 변경된다.
     *
     * @param authentication 현재 로그인한 사용자 인증 정보
     * @param request 비밀번호 변경 요청 DTO
     * @return HTTP 200 OK
     * @author 전진
     * @DateOfCreated 2025-12-30
     * @DateOfEdit 2025-12-30
     */
    @PutMapping
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @RequestBody PasswordChangeRequestDto request
    ) {
        String userId = authentication.getName();

        userProfileService.changePassword(
                userId,
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        return ResponseEntity.ok().build();
    }

}