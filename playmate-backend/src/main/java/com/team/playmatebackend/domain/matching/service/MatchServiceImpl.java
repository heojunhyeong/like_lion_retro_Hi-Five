package com.team.playmatebackend.domain.matching.service;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;
import com.team.playmatebackend.domain.matching.entity.Match;
import com.team.playmatebackend.domain.user.entity.User;
import com.team.playmatebackend.domain.matching.repository.MatchParticipantRepository;
import com.team.playmatebackend.domain.matching.repository.MatchRepository;
import com.team.playmatebackend.domain.user.repository.UserRepository;
import com.team.playmatebackend.global.exception.CustomException;
import com.team.playmatebackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final MatchParticipantRepository matchParticipantRepository;

    @Override
    @Transactional
    public Long createMatch(String userId, MatchCreateDto dto) {
        User host = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Match match = Match.builder()
                .title(dto.getTitle())
                .host(host)
                .category(dto.getCategory())
                .maxParticipants(dto.getMaxParticipants())
                .currentParticipants(1)
                .entryMethod(dto.getEntryMethod())

                // 방장이 설정한 참가 조건 (이 방에 들어올 수 있는 사람의 조건)
                .genderRestriction(dto.getGenderRestriction())
                .ageRestriction(dto.getAgeRestriction())
                .skillRestriction(dto.getSkillRestriction())
                .hashTag(dto.getHashTag())
                .build();

        return matchRepository.save(match).getId();
    }

    @Override
    public void applyToMatch(String userId, Long matchId) {

    }

    @Override
    public void approveParticipant(String hostId, Long participantId) {

    }
}
