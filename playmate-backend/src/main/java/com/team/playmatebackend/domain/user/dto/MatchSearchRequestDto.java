package com.team.playmatebackend.domain.user.dto;

import com.team.playmatebackend.domain.user.entity.enums.MatchSortType;
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
