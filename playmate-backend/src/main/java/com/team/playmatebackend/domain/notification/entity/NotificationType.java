package com.team.playmatebackend.domain.notification.entity;

/**
 * 알림 유형 정의를 위한 Enum 생성
 *
 * @author 김지번
 * @DateOfCreated 2025-12-30
 * @DateOfEdit 2025-12-30
 */

public enum NotificationType {
    MATCH_ENTER("매칭방 입장"),
    MATCH_REQUEST("입장 요청"),
    MATCH_APPROVED("요청 승인");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }
}