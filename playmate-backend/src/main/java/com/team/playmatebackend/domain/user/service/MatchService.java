package com.team.playmatebackend.domain.user.service;

import com.team.playmatebackend.domain.user.dto.MatchResponseDto;
import com.team.playmatebackend.domain.user.dto.MatchSearchRequestDto;

import java.util.List;

public interface MatchService {
    List<MatchResponseDto> searchMatches(MatchSearchRequestDto searchRequest);
}
