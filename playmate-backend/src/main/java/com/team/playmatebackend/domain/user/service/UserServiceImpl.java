package com.team.playmatebackend.domain.user.service;

import com.team.playmatebackend.domain.user.dto.LoginRequestDto;
import com.team.playmatebackend.domain.user.entity.User;
import com.team.playmatebackend.domain.user.repository.UserRepository;
import com.team.playmatebackend.global.Jwt.JwtProvider;
import com.team.playmatebackend.domain.user.dto.UserCreateRequest;
import com.team.playmatebackend.domain.user.entity.enums.UserRoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service //스프링이 빈으로 등록
@RequiredArgsConstructor
//public abstract class UserServiceImpl implements UserService {
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MailService mailService;


    @Override
    public String login(LoginRequestDto request) {
        User user = userRepository.findByUserId(request.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 없습니다"));

        if (!passwordEncoder.matches(
                request.getUserPassword(),
                user.getUserPassword())) {
            throw new IllegalArgumentException("비밀번호다 틀렸습니다");
        }

        return jwtProvider.createToken(user.getUserId());
    }

    // 회원 존재 여부
    @Transactional(readOnly = true)
    public boolean existUser(UserCreateRequest userCreateRequest) {
        return ((userRepository.existsByUserId(userCreateRequest.getUserId())) ||
                userRepository.existsByUserEmail(userCreateRequest.getEmail()));
    }

    /**
     * 회원가입 메서드
     * gender, preferCategory, age는 미선택시 Other을 반환하는 것이 아닌 Null 처리
     *
     * @author 허준형
     * @DateOfCreated 2025-12-23
     * @DateOfEdit 2025-12-23
     */
    @Transactional
    public Long signUp(UserCreateRequest userCreateRequest) {

        if (existUser(userCreateRequest)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        }

        User entity = User.builder()
                .userId(userCreateRequest.getUserId())
                .userPassword(passwordEncoder.encode(userCreateRequest.getPassword()))
                .nickName(userCreateRequest.getNickname())
                .userEmail(userCreateRequest.getEmail())
                .gender(userCreateRequest.getGender())
                .preferCategory(userCreateRequest.getPreferCategory())
                .age(userCreateRequest.getAge())
                .roleType(UserRoleType.USER)
                .build();

        return userRepository.save(entity).getId();
    }

    /**
     * 작성중
     *
     * @author 허준형
     * @DateOfCreated 2025-12-24
     * @DateOfEdit 2025-12-24
     */

    @Transactional
    public void requestPasswordReset(String email) {

        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 유저가 없습니다."));

        // 토큰 생성
        String token = UUID.randomUUID().toString();

        // 만료 시간 설정
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(30);

        // 유저 엔티티에 저장
        user.issuePasswordResetToken(token, expiredAt);

        // 재설정 링크 생성
        String resetLink =
                "http://localhost:3000/password/reset?token=" + token;

        // 메일 전송
        mailService.sendPasswordResetMail(user.getUserEmail(), resetLink);
    }


    @Transactional
    public void resetPassword(String token, String newPassword) {

        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰"));

        if (user.getPasswordResetExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("토큰 만료");
        }

        user.changePassword(passwordEncoder.encode(newPassword));

        user.clearPasswordResetToken(); // 재사용 방지를 위한 토큰 제거
    }


    /**
     *
     * 사용자 로그아웃 로직 구현
     * userId 기반 사용자 조회
     * 로그아웃 시 refresh token 제거 처리
     *
     * @author 김지번
     * @DateOfCreated 2025-12-23
     * @DateOfEdit 2025-12-23
     */
    @Override
    @Transactional
    public void logout(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.logout(); // refreshToken = null
    }
}
