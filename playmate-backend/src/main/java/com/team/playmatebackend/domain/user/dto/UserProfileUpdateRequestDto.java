package com.team.playmatebackend.domain.user.dto;
import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 사용자 프로필 수정 요청 DTO
 *
 * 로그인한 사용자가 자신의 프로필 정보를 수정할 때
 * 클라이언트로부터 전달받는 데이터 구조를 정의한다.
 *
 * 닉네임, 선호 카테고리, 자기소개 항목을 포함한다.
 *
 * @author 전진
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */
@Getter
@NoArgsConstructor
public class UserProfileUpdateRequestDto {
    private String nickName;
    private PreferCategory preferCategory;
    private String introduction;
}