package com.team.playmatebackend.domain.matching.dto;

import com.team.playmatebackend.domain.matching.entity.Match;
import com.team.playmatebackend.domain.user.entity.enums.AgeGroup;
import com.team.playmatebackend.domain.matching.entity.enums.EntryMethod;
import com.team.playmatebackend.domain.user.entity.enums.Gender;
import com.team.playmatebackend.domain.matching.entity.enums.SkillLevel;
import lombok.Getter;

/**
 * 매칭방 상세 조회시 방장이 설정한 정보를 받기 위한 DTO
 *
 * @author 허준형
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */
@Getter
public class MatchDetailResponseDto {
    private Long id;
    private String title;

    // 1. 방장의 정보 (DB에서 가져옴)
    private String hostUserId;
    private String hostNickname;
    private Gender hostGender;
    private AgeGroup hostAge;

    // 2. 방장이 설정한 참가 조건 (이 방에 들어올 수 있는 사람의 조건)
    private Gender genderRestriction;
    private AgeGroup ageRestriction;
    private SkillLevel skillRestriction;

    private int currentParticipants;
    private int maxParticipants;
    private EntryMethod entryMethod;

    public MatchDetailResponseDto(Match match) {
        this.id = match.getId();
        this.title = match.getTitle();

        // 방장 엔티티에서 정보를 가져옴
        this.hostUserId = match.getHost().getUserId();
        this.hostNickname = match.getHost().getNickName();
        this.hostGender = match.getHost().getGender();
        this.hostAge = match.getHost().getAge();

        // 방장이 설정한 참가 조건
        this.genderRestriction = match.getGenderRestriction();
        this.ageRestriction = match.getAgeRestriction();
        this.skillRestriction = match.getSkillRestriction();

        this.currentParticipants = match.getCurrentParticipants();
        this.maxParticipants = match.getMaxParticipants();
        this.entryMethod = match.getEntryMethod();
    }
}