package com.team.playmatebackend.domain.matching.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 매칭방 입장 방식 열거형 클래스
 *
 * @author 허준형
 * @DateOfCreated 2025-12-28
 * @DateOfEdit 2025-12-28
 */
@Getter
@RequiredArgsConstructor
public enum EntryMethod {
    DIRECT("즉시 입장"),
    APPROVAL("방장 승인");
    private final String description;
}
