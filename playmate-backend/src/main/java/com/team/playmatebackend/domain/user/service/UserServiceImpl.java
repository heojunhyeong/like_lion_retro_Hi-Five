package com.team.playmatebackend.domain.user.service;

import com.team.playmatebackend.domain.user.dto.LoginRequestDto;
import com.team.playmatebackend.domain.user.entity.User;
import com.team.playmatebackend.domain.user.repository.UserRepository;
import com.team.playmatebackend.global.Jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            user.getPassword())) {
        throw new IllegalArgumentException("비밀번호다 틀렸습니다");
    }

    return jwtProvider.createToken(user.getUsername());
}
}


