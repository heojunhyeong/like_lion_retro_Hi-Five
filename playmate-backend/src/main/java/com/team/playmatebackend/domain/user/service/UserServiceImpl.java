package com.team.playmatebackend.domain.user.service;

import com.team.playmatebackend.domain.user.dto.LoginRequestDto;
import com.team.playmatebackend.domain.user.entity.User;
import com.team.playmatebackend.domain.user.repository.UserRepository;
import com.team.playmatebackend.global.Jwt.JwtProvider;
import com.team.playmatebackend.domain.user.dto.UserCreateRequest;
import com.team.playmatebackend.domain.user.entity.enums.UserRoleType;
import com.team.playmatebackend.global.exception.CustomException;
import com.team.playmatebackend.global.exception.ErrorCode;
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
     * 패스워드 재설정 메일을 보내는 메서드
     * 재설정 링크에 필요한 토큰, 토큰의 만료시간, 메일 발송을 담당
     *
     *
     * @author 허준형
     * @DateOfCreated 2025-12-24
     * @DateOfEdit 2025-12-26
     */

    @Transactional
    @Override
    public void requestPasswordReset(String email) {

        // 사용자 조회
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 유저가 없습니다."));

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

    /**
     * 위의 메서드로 만들어진 링크를 타고 들어가면 작동하는 패스워드 재설정 메서드
     * 재설정에 성공하면 재사용에 사용된 토큰을 Null처리
     * @author 허준형
     * @DateOfCreated 2025-12-26
     * @DateOfEdit 2025-12-26
     */
    @Transactional
    @Override
    public void resetPassword(String token, String newPassword) {

        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰"));

        if (user.getPasswordResetExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("토큰 만료");
        }

        user.changePassword(passwordEncoder.encode(newPassword));

        // 재사용 방지를 위한 토큰 제거
        user.clearPasswordResetToken();
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
