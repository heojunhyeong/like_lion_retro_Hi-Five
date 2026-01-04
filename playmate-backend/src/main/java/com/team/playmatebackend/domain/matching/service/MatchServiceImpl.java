package com.team.playmatebackend.domain.matching.service;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;
import com.team.playmatebackend.domain.matching.dto.MatchDetailResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchSearchRequestDto;
import com.team.playmatebackend.domain.matching.dto.ParticipantResponseDto;
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
                .hashTag(dto.getHashTag())
                .build();

        Match savedMatch = matchRepository.save(match);

        // 방장을 참가자로 추가
        MatchParticipant hostParticipant = MatchParticipant.builder()
                .match(savedMatch)
                .user(host)
                .status(ParticipantStatus.ACCEPTED)
                .build();

        matchParticipantRepository.save(hostParticipant);

        return savedMatch.getId();
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
     * 참가자 거절 메서드
     *
     * @author 최윤혁
     * @DateOfCreated 2025-12-30
     */
    @Transactional
    @Override
    public void rejectParticipant(String hostId, Long participantId) {
        MatchParticipant participant = matchParticipantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("참가 신청 내역이 없습니다."));

        // 방장 권한 확인
        if (!participant.getMatch().getHost().getUserId().equals(hostId)) {
            throw new IllegalArgumentException("방장만 거절할 수 있습니다.");
        }

        /**
         * 234~236, 242~249행 추가
         * 거절 알림 전송 파트 추가
         *
         * @author 김지번
         * @DateOfCreated 2025-12-31
         * @DateOfEdit 2025-12-31
         */

        // 알림 전송을 위해 필요한 정보 미리 저장 (삭제 전)
        User targetUser = participant.getUser();
        String matchTitle = participant.getMatch().getTitle();

        // 상태를 거절로 변경 및 참여 내역 삭제
        participant.reject();
        matchParticipantRepository.delete(participant);

        // 거절 알림 전송 (추가된 부분)
        notificationService.send(
                targetUser.getUserId(),
                NotificationType.MATCH_REJECTED,
                matchTitle + " 방 입장이 거절되었습니다.",
                ""
        );

    }

    /**
     * 매칭방 참가자 목록 조회
     *
     * @author 최윤혁
     * @DateOfCreated 2025-12-30
     */
    @Override
    public List<ParticipantResponseDto> getParticipants(Long matchId) {
        List<MatchParticipant> participants = matchParticipantRepository.findByMatchId(matchId);
        return participants.stream()
                .map(participant -> ParticipantResponseDto.builder()
                        .participantId(participant.getId())
                        .userId(participant.getUser().getUserId())
                        .nickName(participant.getUser().getNickName())
                        .gender(participant.getUser().getGender())
                        .age(participant.getUser().getAge())
                        .status(participant.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 매칭방 상세 정보 조회
     *
     * @author 최윤혁
     * @DateOfCreated 2025-12-30
     */
    @Override
    public MatchDetailResponseDto getMatchDetail(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        return new MatchDetailResponseDto(match);
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

        // 해당 유저가 실제로 매칭방에 참여 중인지 검증
        MatchParticipant participant = matchParticipantRepository.findByMatchIdAndUserUserId(matchId, userId)
                .orElseThrow(() -> new IllegalArgumentException("참여중 아님"));

        Match match = participant.getMatch();

        // 상태 변수 체크
        boolean isHost = match.getHost().getUserId().equals(userId);

        // 인원 감소 및 리스트에서 제거
        match.removeParticipant();
        match.getParticipants().remove(participant);

        // 현재 인원 확인
        boolean isEmpty = match.getCurrentParticipants() <= 0;

        if (isHost || isEmpty) {
            matchRepository.delete(match);
        } else {
            // 방장이 아니고 인원도 남았다면 본인 참여 정보만 삭제
            matchParticipantRepository.delete(participant);
        }
    }


    // 매칭방과 유저의 조건이 맞는지 검증하는 메서드
    // 실력 제한은 검증하지 않음 (어떤 실력이어도 입장 가능)
    private void validateParticipantConditions(User user, Match match) {

        if (match.getGenderRestriction() != null && !user.getGender().equals(match.getGenderRestriction())) {
            throw new IllegalArgumentException("참가 가능한 성별이 아닙니다.");
        }

        if (match.getAgeRestriction() != null && !user.getAge().equals(match.getAgeRestriction())) {
            throw new IllegalArgumentException("참가 가능한 나이대가 아닙니다");
        }

        // 실력 제한 검증 제거 - 실력과 관계없이 입장 가능
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


    /**
     * 채팅방 권한 검증 메서드
     * 해당 매칭방에 참여 정보가 있고 상태가 ACCEPTED인 경우에만 입장을 허용한다
     *
     * @author 허준형
     * @DateOfCreated 2025-12-29
     * @DateOfEdit 2025-12-29
     */
//    @Override
//    @Transactional(readOnly = true)
//    public void checkChatAccess(String userId, Long matchId) {
//
//        boolean isAuthorized = matchParticipantRepository.findByMatchIdAndUserUserId(matchId, userId)
//                .map(participant -> participant.getStatus() == ParticipantStatus.ACCEPTED)
//                .orElse(false);
//
//        if (!isAuthorized) {
//            throw new CustomException(ErrorCode.ACCESS_DENIED);
//        }
//    }
}