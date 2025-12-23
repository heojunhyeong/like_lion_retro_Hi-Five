package com.team.playmatebackend.domain.user.dto;

import com.team.playmatebackend.domain.user.entity.enums.AgeGroup;
import com.team.playmatebackend.domain.user.entity.enums.Gender;
import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import lombok.Getter;

@Getter
public class UserCreateRequest {
    private String userId;
    private String password;
    private String nickname;
    private String email;

    private Gender gender;
    private PreferCategory preferCategory;
    private AgeGroup age;
}
