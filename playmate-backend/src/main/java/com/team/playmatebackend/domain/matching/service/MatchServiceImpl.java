package com.team.playmatebackend.domain.matching.service;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;
import com.team.playmatebackend.domain.matching.dto.MatchResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchSearchRequestDto;
import com.team.playmatebackend.domain.matching.entity.Match;
import com.team.playmatebackend.domain.matching.entity.MatchParticipant;
import com.team.playmatebackend.domain.matching.entity.enums.EntryMethod;
import com.team.playmatebackend.domain.matching.entity.enums.ParticipantStatus;
import com.team.playmatebackend.domain.notification.entity.NotificationType;
import com.team.playmatebackend.domain.notification.service.NotificationService;
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
    private final NotificationService notificationService;

    /**
     * 매칭방 생성 메서드
     * 성별, 나이, 실력 등의 조건은 필수가 아닌 선택 옵션
     *
     * @author 허준형
     * @DateOfCreated 2025-12-29
     * @DateOfEdit 2025-12-29
     */
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
                .build();

        return matchRepository.save(match).getId();
    }

    /**
     * 매칭방 입장 메서드
     * 방장이 매칭방을 생성할 때 위에 명시한 3가지 조건 중 하나라도 명시했다면
     * validateparticipantConditions에서 참가자의 조건과 명시한 조건이 맞는지 검증
     * 방장이 즉시 입장이 아닌 수락 대기 상태로 방을 만들었다면 대기
     *
     * @author 허준형
     * @DateOfCreated 2025-12-29
     * @DateOfEdit 2025-12-29
     */
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

        /**
         * 알림 전송 로직을 별도 메서드로 분리하여 호출
         *
         * @author 김지번
         * @DateOfCreated 2025-12-30
         * @DateOfEdit 2025-12-30
         */

        sendApplyNotification(match, user, status);
    }

    /**
     * 입장 상태(즉시 입장/대기)에 따라 적절한 알림을 전송하는 헬퍼 메서드
     *
     * @author 김지번
     * @DateOfCreated 2025-12-30
     * @DateOfEdit 2025-12-30
     */
    private void sendApplyNotification(Match match, User user, ParticipantStatus status) {
        String hostId = match.getHost().getUserId();
        String matchTitle = match.getTitle();
        String userNickname = user.getNickName();

        if (status == ParticipantStatus.ACCEPTED) {
            // 1. 즉시 입장 -> "입장 알림"
            notificationService.send(
                    hostId,
                    NotificationType.MATCH_ENTER,
                    userNickname + "님이 " + matchTitle + " 방에 입장했습니다.",
                    "/matches/" + match.getId()
            );
        } else if (status == ParticipantStatus.WAITING) {
            // 2. 승인 대기 -> "요청 알림"
            notificationService.send(
                    hostId,
                    NotificationType.MATCH_REQUEST,
                    userNickname + "님이 " + matchTitle + " 방 입장을 요청했습니다.",
                    "/matches/" + match.getId() + "/manage"
            );
        }
    }


    /**
     * 방장의 승인 메서드
     *
     * @author 허준형
     * @DateOfCreated 2025-12-29
     * @DateOfEdit 2025-12-29
     */
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

        /**
         * 승인 완료 -> 신청자에게 "승인되었다"고 알림
         * 받는 사람: 신청자 (participant.getUser().getUserId())
         *
         * @author 김지번
         * @DateOfCreated 2025-12-30
         * @DateOfEdit 2025-12-30
         */

        User targetUser = participant.getUser();

        notificationService.send(
                targetUser.getUserId(),
                NotificationType.MATCH_APPROVED,
                participant.getMatch().getTitle() + " 방 입장이 승인되었습니다.",
                "/matches/" + participant.getMatch().getId() // 해당 매칭방으로 이동
        );

    }


    /**
     * 방 나가기 및 자동 삭제 메서드
     * 일반 참여자가 나갈 때는 인원수만 줄어들고, 마지막 인원이었다면 방을 삭제함
     * 방장이 나갈때는 바로 방을 삭제함
     * @author 허준형
     * @DateOfCreated 2025-12-29
     * @DateOfEdit 2025-12-29
     */
    @Transactional
    @Override
    public void leaveMatch(String userId, Long matchId) {

        // 해당 유저가 실제로 매칭방에 참여중인지 검증
        MatchParticipant participant = matchParticipantRepository.findByMatchIdAndUserUserId(matchId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매칭에 참여하고 있지 않습니다"));

        Match match = participant.getMatch();

        // 참여 내역 삭제
        matchParticipantRepository.delete(participant);

        // 인원 수 감소
        match.removeParticipant();

        // 방이 비었거나, 나가는 사람이 방장인 경우 방을 삭제
        if (match.getCurrentParticipants() <= 0 || match.getHost().getUserId().equals(userId)) {
            // 방 삭제
            matchRepository.delete(match);
        }
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