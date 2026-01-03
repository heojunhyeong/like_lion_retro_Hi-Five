package com.team.playmatebackend.domain.matching.controller;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;
import com.team.playmatebackend.domain.matching.dto.MatchDetailResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchSearchRequestDto;
import com.team.playmatebackend.domain.matching.dto.ParticipantResponseDto;
import com.team.playmatebackend.domain.matching.entity.enums.MatchSortType;
import com.team.playmatebackend.domain.matching.service.MatchService;
import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import com.team.playmatebackend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 매칭방 생성 Controller
 *
 *
 * @author 허준형
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    /**
     * 매칭방 생성
     * Authentication 객체를 받아서 처리
     *
     * @author 허준형
     * @DateOfCreated 2025-12-29
     * @DateOfEdit 2025-12-29
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createMatch(
            Authentication authentication,
            @RequestBody MatchCreateDto dto
    ) {
        String userId = authentication.getName();
        Long matchId = matchService.createMatch(userId, dto);
        return ResponseEntity.ok(ApiResponse.success(matchId));

    }

    /**
     * 매칭방 퇴장
     *
     * @author 허준형
     * @DateOfCreated 2025-12-29
     * @DateOfEdit 2025-12-29
     */
    @PostMapping("/{matchId}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveMatch(
            Authentication authentication,
            @PathVariable Long matchId
    ) {
        matchService.leaveMatch(authentication.getName(), matchId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    //매칭방 검색
    @GetMapping
    public ResponseEntity<List<MatchResponseDto>> getMatches(
            @RequestParam(required = false) PreferCategory preferCategory,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) MatchSortType sortType
    ) {
        MatchSearchRequestDto searchRequest = MatchSearchRequestDto.builder()
                .preferCategory(preferCategory)
                .keyword(keyword)
                .sortType(sortType)
                .build();

        List<MatchResponseDto> matches = matchService.searchMatches(searchRequest);
        return ResponseEntity.ok(matches);
    }

    /**
     * 매칭방 상세 정보 조회
     *
     * @author 최윤혁
     * @DateOfCreated 2025-12-30
     */
    @GetMapping("/{matchId}")
    public ResponseEntity<ApiResponse<MatchDetailResponseDto>> getMatchDetail(@PathVariable Long matchId) {
        MatchDetailResponseDto matchDetail = matchService.getMatchDetail(matchId);
        return ResponseEntity.ok(ApiResponse.success(matchDetail));
    }

    /**
     * 매칭방 참가자 목록 조회
     *
     * @author 최윤혁
     * @DateOfCreated 2025-12-30
     */
    @GetMapping("/{matchId}/participants")
    public ResponseEntity<ApiResponse<List<ParticipantResponseDto>>> getParticipants(@PathVariable Long matchId) {
        List<ParticipantResponseDto> participants = matchService.getParticipants(matchId);
        return ResponseEntity.ok(ApiResponse.success(participants));
    }

    /**
     * 참가자 승인
     *
     * @author 최윤혁
     * @DateOfCreated 2025-12-30
     */
    @PostMapping("/participants/{participantId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveParticipant(
            Authentication authentication,
            @PathVariable Long participantId
    ) {
        String hostId = authentication.getName();
        matchService.approveParticipant(hostId, participantId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 참가자 거절
     *
     * @author 최윤혁
     * @DateOfCreated 2025-12-30
     */
    @PostMapping("/participants/{participantId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectParticipant(
            Authentication authentication,
            @PathVariable Long participantId
    ) {
        String hostId = authentication.getName();
        matchService.rejectParticipant(hostId, participantId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    //매칭방 상세 조회
    //@GetMapping("/{id}")

}
