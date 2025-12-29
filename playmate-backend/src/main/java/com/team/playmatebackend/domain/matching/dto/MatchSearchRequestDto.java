package com.team.playmatebackend.domain.matching.dto;

import com.team.playmatebackend.domain.matching.entity.enums.MatchSortType;
import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
//검색 요청 Dto
public class MatchSearchRequestDto {
    private PreferCategory preferCategory;
    private String keyword;
    private MatchSortType sortType;
}
