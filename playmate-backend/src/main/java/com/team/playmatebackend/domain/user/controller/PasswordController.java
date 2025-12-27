package com.team.playmatebackend.domain.user.controller;

import com.team.playmatebackend.domain.user.dto.PasswordResetConfirmDto;
import com.team.playmatebackend.domain.user.dto.PasswordResetRequestDto;
import com.team.playmatebackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 비밀번호 재설정 요청을 처리하는 API 컨트롤러
 *
 * @author 허준형
 * @DateOfCreated 2025-12-27
 * @DateOfEdit 2025-12-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/password")
public class PasswordController {

    private final UserService userService;

    // 비밀번호 재설정 메일 요청

    @PostMapping("/reset/request")
    public ResponseEntity<Void> requestReset(
            @RequestBody PasswordResetRequestDto dto
    ) {
        userService.requestPasswordReset(dto.getEmail());
        return ResponseEntity.ok().build();
    }

    // 비밀번호 재설정 처리
    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(
            @RequestBody PasswordResetConfirmDto dto
    ) {
        userService.resetPassword(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
