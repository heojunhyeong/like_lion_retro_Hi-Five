package com.team.playmatebackend.domain.matching.service;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;
import com.team.playmatebackend.domain.matching.dto.MatchDetailResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchSearchRequestDto;
import com.team.playmatebackend.domain.matching.dto.ParticipantResponseDto;

import java.util.List;

public interface MatchService {

    Long createMatch(String userId, MatchCreateDto dto);
    void applyToMatch(String userId, Long matchId);
    void approveParticipant(String hostId, Long participantId);
    void rejectParticipant(String hostId, Long participantId);
    void leaveMatch(String userId, Long matchId);
    List<MatchResponseDto> searchMatches(MatchSearchRequestDto searchRequest);
    List<ParticipantResponseDto> getParticipants(Long matchId);
    MatchDetailResponseDto getMatchDetail(Long matchId);
}
