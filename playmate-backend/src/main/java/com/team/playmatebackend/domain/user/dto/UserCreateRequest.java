package com.team.playmatebackend.domain.user.dto;

import lombok.Getter;

@Getter
public class UserCreateRequest {
    private String userId;
    private String password;
    private String nickname;
    private String email;
}
