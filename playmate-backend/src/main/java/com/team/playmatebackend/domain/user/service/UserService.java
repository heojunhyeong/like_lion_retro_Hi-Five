package com.team.playmatebackend.domain.user.service;

import com.team.playmatebackend.domain.user.dto.LoginRequestDto;

public interface UserService {

    String login(LoginRequestDto request); // 로그인 기능 선언
}
