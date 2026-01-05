package com.team.playmatebackend.domain.matching.dto;

import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
//검색 응답 Dto
public class MatchResponseDto {
    private Long id;
    private String title;
    private String hostNickName;
    private PreferCategory preferCategory;
    private int currentParticipants;
    private int maxParticipants;
    private LocalDateTime createdDate;
}
