package com.team.playmatebackend.domain.user.service;

import com.team.playmatebackend.domain.user.dto.UserCreateRequest;
import com.team.playmatebackend.domain.user.entity.User;
import com.team.playmatebackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

// 회원 존재 여부
    @Transactional(readOnly = true)
    public boolean existUser(UserCreateRequest userCreateRequest) {
        return ((userRepository.existsByUserId(userCreateRequest.getUserId())) ||
                userRepository.existByEmail(userCreateRequest.getEmail()));
    }


// 회원 가입
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
                .build();

        return userRepository.save(entity).getId();
    }


}