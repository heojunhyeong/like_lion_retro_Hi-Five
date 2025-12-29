package com.team.playmatebackend.domain.user.controller;

import com.team.playmatebackend.domain.user.dto.MatchResponseDto;
import com.team.playmatebackend.domain.user.dto.MatchSearchRequestDto;
import com.team.playmatebackend.domain.user.entity.enums.MatchSortType;
import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import com.team.playmatebackend.domain.user.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<MatchResponseDto>> getMatches(@ModelAttribute MatchSearchRequestDto searchRequest) { //카테고리, 키워드, 인원
        List<MatchResponseDto> matches = matchService.searchMatches(searchRequest);
        return ResponseEntity.ok(matches);
    }
}
