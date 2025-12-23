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

@Service //스프링이 빈으로 등록
@RequiredArgsConstructor
public abstract class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    @Override
    public String login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 없습니다"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getUserPassword())) {
            throw new IllegalArgumentException("비밀번호다 틀렸습니다");
        }

        return jwtProvider.createToken(user.getUserPassword());
    }


    // 회원 존재 여부
    @Transactional(readOnly = true)
    public boolean existUser(UserCreateRequest userCreateRequest) {
        return ((userRepository.existsByUserId(userCreateRequest.getUserId())) ||
                userRepository.existByEmail(userCreateRequest.getEmail()));
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


}
