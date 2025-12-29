package com.team.playmatebackend.domain.matching.service;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;
import com.team.playmatebackend.domain.matching.dto.MatchResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchSearchRequestDto;
import com.team.playmatebackend.domain.matching.entity.Match;
import com.team.playmatebackend.domain.user.entity.User;
import com.team.playmatebackend.domain.matching.repository.MatchParticipantRepository;
import com.team.playmatebackend.domain.matching.repository.MatchRepository;
import com.team.playmatebackend.domain.matching.entity.enums.MatchSortType;
import com.team.playmatebackend.domain.user.repository.UserRepository;
import com.team.playmatebackend.global.exception.CustomException;
import com.team.playmatebackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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


                // 방장이 조건을 설정하면 Match 엔티티에 저장된다
                .genderRestriction(dto.getGenderRestriction())
                .ageRestriction(dto.getAgeRestriction())
                .skillRestriction(dto.getSkillRestriction())
                .build();

        return matchRepository.save(match).getId();
    }

    @Override
    public void applyToMatch(String userId, Long matchId) {

    }

    @Override
    public void approveParticipant(String hostId, Long participantId) {

    }
    @Override
    public List<MatchResponseDto> searchMatches(MatchSearchRequestDto searchRequest) {
        Specification<Match> spec = (root, query, cb) -> cb.conjunction();

        if (searchRequest.getPreferCategory() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category"), searchRequest.getPreferCategory())
            );
        }

        if (searchRequest.getKeyword() != null && !searchRequest.getKeyword().trim().isEmpty()) {
            String keyword = "%" + searchRequest.getKeyword().trim() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("title"), keyword)
            );
        }

        MatchSortType sortType = searchRequest.getSortType() != null
                ? searchRequest.getSortType()
                : MatchSortType.LATEST;

        Sort sort;
        switch (sortType) {
            case PARTICIPANTS:
                sort = Sort.by(Sort.Direction.DESC, "currentParticipants");
                break;
            case LATEST:
            default:
                sort = Sort.by(Sort.Direction.DESC, "createdDate");
                break;
        }

        List<Match> matches = matchRepository.findAll(spec, sort);

        return matches.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private MatchResponseDto toResponseDto(Match match) {
        return MatchResponseDto.builder()
                .id(match.getId())
                .title(match.getTitle())
                .hostNickName(match.getHost().getNickName())
                .preferCategory(match.getCategory())
                .currentParticipants(match.getCurrentParticipants())
                .maxParticipants(match.getMaxParticipants())
                .createdDate(match.getCreatedDate())
                .build();
    }

}
