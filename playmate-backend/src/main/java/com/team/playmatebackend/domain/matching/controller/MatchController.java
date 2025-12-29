package com.team.playmatebackend.domain.matching.controller;

import com.team.playmatebackend.domain.matching.dto.MatchCreateDto;
import com.team.playmatebackend.domain.matching.service.MatchService;
import com.team.playmatebackend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
     * 매칭방 생성 메서드
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


}
