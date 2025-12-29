package com.team.playmatebackend.domain.user.service;

import com.team.playmatebackend.domain.user.dto.MatchResponseDto;
import com.team.playmatebackend.domain.user.dto.MatchSearchRequestDto;
import com.team.playmatebackend.domain.user.entity.Match;
import com.team.playmatebackend.domain.user.entity.enums.MatchSortType;
import com.team.playmatebackend.domain.user.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

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
