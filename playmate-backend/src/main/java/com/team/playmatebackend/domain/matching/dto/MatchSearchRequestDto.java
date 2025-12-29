package com.team.playmatebackend.domain.matching.dto;

import com.team.playmatebackend.domain.matching.entity.enums.MatchSortType;
import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//검색 요청 Dto
public class MatchSearchRequestDto {
    private PreferCategory preferCategory;
    private String keyword;
    private MatchSortType sortType;

    //@Builder과 @NoArgsConstructor 와 같이 사용하려면 생성자를 추가해줘야함.
    @Builder
    public MatchSearchRequestDto(PreferCategory preferCategory, String keyword, MatchSortType sortType) {
        this.preferCategory = preferCategory;
        this.keyword = keyword;
        this.sortType = sortType;
    }
}
