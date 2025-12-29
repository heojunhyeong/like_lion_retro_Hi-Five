package com.team.playmatebackend.domain.matching.dto;

import com.team.playmatebackend.domain.matching.entity.enums.EntryMethod;
import com.team.playmatebackend.domain.matching.entity.enums.SkillLevel;
import com.team.playmatebackend.domain.user.entity.enums.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 매칭방 생성을 위한 DTO
 *
 *
 * @author 허준형
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */
@Getter
@NoArgsConstructor
public class MatchCreateDto {
    private String title;
    private PreferCategory category;
    private int maxParticipants;
    private EntryMethod entryMethod;

    private Gender genderRestriction;
    private AgeGroup ageRestriction;
    private SkillLevel skillRestriction;

    private String contactInfo;


}
