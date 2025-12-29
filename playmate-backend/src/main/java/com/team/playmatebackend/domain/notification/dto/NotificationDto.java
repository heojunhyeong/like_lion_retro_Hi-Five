package com.team.playmatebackend.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 알림 데이터 구조 정의
 *
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private String type;      // ROOM_JOIN_REQUEST, ROOM_JOINED, ROOM_JOIN_APPROVED
    private String message;   // 알림 메시지
    private String roomId;    // 매칭방 ID
    private String userId;    // 관련 유저 ID
}
