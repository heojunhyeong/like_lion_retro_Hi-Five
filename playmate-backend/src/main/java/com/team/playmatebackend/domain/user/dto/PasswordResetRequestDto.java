package com.team.playmatebackend.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비밀번호 재설정 요청 DTO
 * 비밀번호를 재설정할 메일을 받는 DTO
 *
 * @author 허준형
 * @DateOfCreated 2025-12-27
 * @DateOfEdit 2025-12-27
 */

@Getter
@NoArgsConstructor
public class PasswordResetRequestDto {
    private String email;
}
