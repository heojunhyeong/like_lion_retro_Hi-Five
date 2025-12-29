package com.team.playmatebackend.domain.user.service;

import com.team.playmatebackend.domain.user.dto.LoginRequestDto;
import com.team.playmatebackend.domain.user.dto.LoginResponseDto;
import com.team.playmatebackend.domain.user.dto.PasswordResetToken;
import com.team.playmatebackend.domain.user.entity.User;
import com.team.playmatebackend.domain.user.repository.PasswordResetTokenRepository;
import com.team.playmatebackend.domain.user.repository.UserRepository;
import com.team.playmatebackend.global.jwt.JwtProvider;
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
    private final PasswordResetTokenRepository passwordResetTokenRepository;


    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUserId(request.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 없습니다"));

        if (!passwordEncoder.matches(
                request.getUserPassword(),
                user.getUserPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다");
        }

        // Access Token 생성
        String accessToken = jwtProvider.createAccessToken(user.getUserId());
        
        // Refresh Token 생성
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());
        
        // Refresh Token을 DB에 저장
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
        
        // 두 토큰을 함께 반환
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public String refreshAccessToken(String refreshToken) {
        // Refresh Token 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다");
        }
        
        // DB에서 해당 Refresh Token을 가진 사용자 조회
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh Token을 찾을 수 없습니다"));
        
        // 새로운 Access Token 발급
        String newAccessToken = jwtProvider.createAccessToken(user.getUserId());
        
        return newAccessToken;
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
     * @TODO 주석 재작성 필요
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

        // 토큰 생성
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiredAt(LocalDateTime.now().plusMinutes(30))
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);

        String resetLink =
                "http://localhost:3000/password/reset?token=" + token;

        mailService.sendPasswordResetMail(user.getUserEmail(), resetLink);

    }

    /**
     * 유저가 새로운 비밀번호를 입력 후 제출할 때 사용되는 메서드
     *
     * @author 허준형
     * @DateOfCreated 2025-12-26
     * @DateOfEdit 2025-12-26
     */
    @Transactional
    @Override
    public void resetPassword(String token, String newPassword) {

        // DB에 있는지 체크
        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByToken(token)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESET_TOKEN_NOT_FOUND));

        // 이미 사용된 토큰인지 검증
        if (resetToken.isUsed()) {
            throw new CustomException(ErrorCode.RESET_TOKEN_ALREADY_USED);
        }

        // 토큰이만료됐는지 체크
        if (resetToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.RESET_TOKEN_EXPIRED);
        }

        // 유저를 가져온 뒤
        User user = resetToken.getUser();

        // 비밀번호 변경
        user.changePassword(passwordEncoder.encode(newPassword));

        // used가 true로 바뀌며 해당 토큰은 재사용 불가 처리
        resetToken.use();
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
