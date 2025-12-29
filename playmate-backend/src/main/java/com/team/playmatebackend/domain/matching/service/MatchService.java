package com.team.playmatebackend.domain.matching.service;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;
import com.team.playmatebackend.domain.matching.dto.MatchResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchSearchRequestDto;

import java.util.List;

public interface MatchService {

    Long createMatch(String userId, MatchCreateDto dto);
    void applyToMatch(String userId, Long matchId);
    void approveParticipant(String hostId, Long participantId);
    List<MatchResponseDto> searchMatches(MatchSearchRequestDto searchRequest);
}
