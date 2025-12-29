package com.team.playmatebackend.domain.matching.service;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;
import com.team.playmatebackend.domain.matching.dto.MatchResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchSearchRequestDto;
import com.team.playmatebackend.domain.matching.entity.Match;
import com.team.playmatebackend.domain.matching.entity.MatchParticipant;
import com.team.playmatebackend.domain.matching.entity.enums.EntryMethod;
import com.team.playmatebackend.domain.matching.entity.enums.ParticipantStatus;
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
                .contactInfo(dto.getContactInfo())
                .maxParticipants(dto.getMaxParticipants())
                .currentParticipants(1)
                .entryMethod(dto.getEntryMethod())

                // 방장이 조건을 설정하면 Match 엔티티에 저장된다
                .genderRestriction(dto.getGenderRestriction())
                .ageRestriction(dto.getAgeRestriction())
                .skillRestriction(dto.getSkillRestriction())
                .hashTag(dto.getHashTag())
                .build();

        return matchRepository.save(match).getId();
    }

    @Transactional
    @Override
    public void applyToMatch(String userId, Long matchId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        // 방장의 요구조건 검증
        validateParticipantConditions(user, match);

        // 입장 방식에 따른 상태 결정
        // 즉시 입장 받이면 자동수락, 아니면 대기
        ParticipantStatus status = (match.getEntryMethod() == EntryMethod.DIRECT)
                ? ParticipantStatus.ACCEPTED
                : ParticipantStatus.WAITING;

        if (status == ParticipantStatus.ACCEPTED) {
            match.addParticipant();
        }

        MatchParticipant participant = MatchParticipant.builder()
                .match(match)
                .user(user)
                .status(status)
                .build();


        matchParticipantRepository.save(participant);
    }

    @Transactional
    @Override
    public void approveParticipant(String hostId, Long participantId) {
        MatchParticipant participant = matchParticipantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("참가 신청 내역이 없습니다."));

        // 방장 권한 확인
        if (!participant.getMatch().getHost().getUserId().equals(hostId)) {
            throw new IllegalArgumentException("방장만 승인할 수 있습니다.");
        }

        // 인원 추가 및 상태 변경
        participant.getMatch().addParticipant();
        participant.approve();
    }

    // 매칭방과 유저의 조건이 맞는지 검증하는 메서드
    private void validateParticipantConditions(User user, Match match) {

        if (match.getGenderRestriction() != null && !user.getGender().equals(match.getGenderRestriction())) {
            throw new IllegalArgumentException("참가 가능한 성별이 아닙니다.");
        }

        if (match.getAgeRestriction() != null && !user.getAge().equals(match.getAgeRestriction())) {
            throw new IllegalArgumentException("참가 가능한 나이대가 아닙니다");
        }

        if (match.getSkillRestriction() != null && !user.getSkillRestriction().equals(match.getSkillRestriction())) {
            throw new IllegalArgumentException(("참가 가능한 실력이 아닙니다"));
        }
    }

    //Specification<엔티티> 문법 - 검색 대상 엔티티,
    private Specification<Match> buildSearchSpec(MatchSearchRequestDto request) {
        Specification<Match> spec = (root, query, cb) -> cb.conjunction(); //항상 참, 조건이 없는 상태

        //카테고리 필터
        if (request.getPreferCategory() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category"), request.getPreferCategory())
            );
        }

        //방제목 키워드 검색
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String keyword = "%" + request.getKeyword().trim() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("title"), keyword)
            );
        }
        return spec;
    }

    // 정렬 필터 (참여자 수 순, 최신순)
    private Sort buildSort(MatchSortType sortType) {
        //기본으로 최신순으로 정렬
        if (sortType == null) {
            return Sort.by(Sort.Direction.DESC, "createdDate");
        }

        switch (sortType) {
            case PARTICIPANTS:
                return Sort.by(Sort.Direction.DESC, "currentParticipants"); // 인원순
            case LATEST:
            default:
                return Sort.by(Sort.Direction.DESC, "createdDate"); //최신순
        }
    }

    //검색조건 + 정렬 조합 후 DB에 조회 및 DTO 반환
    @Override
    public List<MatchResponseDto> searchMatches(MatchSearchRequestDto request) {

        Specification<Match> spec = buildSearchSpec(request);   // 검색 조건
        Sort sort = buildSort(request.getSortType());   // 정렬

        return matchRepository.findAll(spec, sort).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // Match 엔티티 -> MatchResponseDto 변환 메서드
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