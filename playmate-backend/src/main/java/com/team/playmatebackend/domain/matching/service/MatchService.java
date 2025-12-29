package com.team.playmatebackend.domain.matching.service;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;

public interface MatchService {

    Long createMatch(String userId, MatchCreateDto dto);
    void applyToMatch(String userId, Long matchId);
    void approveParticipant(String hostId, Long participantId);
}
