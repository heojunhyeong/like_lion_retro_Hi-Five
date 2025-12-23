package com.team.playmatebackend.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    private String userID; // 클라이언트에서 보낸 아이디
    private String userPassword; // 클라이언트에서 보낸 비밀번호
}
