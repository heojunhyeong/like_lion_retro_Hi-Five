package com.team.playmatebackend.domain.user.dto;

import com.team.playmatebackend.domain.user.entity.enums.AgeGroup;
import com.team.playmatebackend.domain.user.entity.enums.Gender;
import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 프로필 조회 응답 DTO
 *
 * 로그인한 사용자의 프로필 정보를 클라이언트로 전달하기 위한 객체이다.
 * 비밀번호와 같은 민감 정보는 포함하지 않으며,
 * 프로필 화면에 필요한 정보만을 담는다.
 *
 * @author 전진
 * @DateOfCreated 2025-12-27
 * @DateOfEdit 2025-12-27
 */
@Getter
@AllArgsConstructor
public class UserProfileResponseDto {

    private String userId;
    private String userEmail;
    private String nickName;
    private PreferCategory preferCategory;
    private Gender gender;
    private AgeGroup age;
    private String introduction; // null 가능

}

