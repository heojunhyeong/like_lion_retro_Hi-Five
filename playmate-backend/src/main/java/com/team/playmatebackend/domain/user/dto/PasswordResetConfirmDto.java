package com.team.playmatebackend.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비밀번호 변경 DTO
 * 토큰과 새로운 비밀번호만 받고 이외 다른 사용자 정보는 받지 않는다(보안)
 *
 * @author 허준형
 * @DateOfCreated 2025-12-27
 * @DateOfEdit 2025-12-27
 */
@Getter
@NoArgsConstructor
public class PasswordResetConfirmDto {
    private String token;
    private String newPassword;
}
