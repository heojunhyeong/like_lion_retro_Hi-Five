package com.team.playmatebackend.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 비밀번호 변경 요청 DTO
 *
 * 로그인한 사용자가 자신의 비밀번호를 변경할 때 사용된다.
 * 기존 비밀번호와 변경할 새 비밀번호를 함께 전달받는다.
 *
 * 이 DTO는 Controller 계층에서 요청 바디를 매핑하는 용도로 사용된다.
 *
 * @author 전진
 * @DateOfCreated 2025-12-30
 * @DateOfEdit 2025-12-30
 */
@Getter
@NoArgsConstructor
public class PasswordChangeRequestDto {

    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirm;
}

