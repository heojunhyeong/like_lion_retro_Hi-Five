package com.team.playmatebackend.domain.user.controller;

import com.team.playmatebackend.domain.user.dto.LoginRequestDto;
import com.team.playmatebackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //rest api 컨트롤러
@RequiredArgsConstructor //생성사 자동 생성
@RequestMapping("/api/users")  //기본 url
public class UserController {
    private final UserService userService;

@PostMapping("/login")
public ResponseEntity<String> login(@RequestBody LoginRequestDto request){  //json을 dto변환

    String token = userService.login(request); // 로그인 + JWT 발급
    return ResponseEntity.ok(token);            // 토큰 반환
}
}