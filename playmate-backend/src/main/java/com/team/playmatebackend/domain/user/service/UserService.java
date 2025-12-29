/**
 *
 * UserService에 로그아웃 메서드 선언
 *
 * @author 김지번
 * @DateOfCreated 2025-12-23
 * @DateOfEdit 2025-12-23
 */

package com.team.playmatebackend.domain.user.service;

import com.team.playmatebackend.domain.user.dto.LoginRequestDto;
import com.team.playmatebackend.domain.user.dto.LoginResponseDto;
import com.team.playmatebackend.domain.user.dto.UserCreateRequest;

public interface UserService {
    void logout(String userId);

    LoginResponseDto login(LoginRequestDto request); // 로그인 기능 선언
    String refreshAccessToken(String refreshToken); // Refresh Token으로 새 Access Token 발급
    Long signUp(UserCreateRequest request);

    void requestPasswordReset(String email);

    void resetPassword(String token, String newPassword);
}
