package com.team.playmatebackend.domain.matching.dto;

import com.team.playmatebackend.domain.matching.entity.enums.ParticipantStatus;
import com.team.playmatebackend.domain.user.entity.enums.AgeGroup;
import com.team.playmatebackend.domain.user.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 참가자 정보 응답 DTO
 * 
 * @author 최윤혁
 * @DateOfCreated 2025-12-30
 */
@Getter
@Builder
@AllArgsConstructor
public class ParticipantResponseDto {
    private Long participantId;
    private String userId;
    private String nickName;
    private Gender gender;
    private AgeGroup age;
    private ParticipantStatus status;
}


