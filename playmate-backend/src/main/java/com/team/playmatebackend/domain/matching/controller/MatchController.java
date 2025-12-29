package com.team.playmatebackend.domain.matching.controller;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;
import com.team.playmatebackend.domain.matching.dto.MatchResponseDto;
import com.team.playmatebackend.domain.matching.dto.MatchSearchRequestDto;
import com.team.playmatebackend.domain.matching.service.MatchService;
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
    public ResponseEntity<List<MatchResponseDto>> getMatches(@ModelAttribute MatchSearchRequestDto searchRequest) { //카테고리, 키워드, 인원
        List<MatchResponseDto> matches = matchService.searchMatches(searchRequest);
        return ResponseEntity.ok(matches);
    }

    //매칭방 상세 조회
    //@GetMapping("/{id}")

}
