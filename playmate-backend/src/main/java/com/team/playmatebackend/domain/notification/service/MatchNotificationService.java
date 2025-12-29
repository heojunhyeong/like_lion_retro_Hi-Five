package com.team.playmatebackend.domain.notification.service;

import com.team.playmatebackend.domain.notification.dto.NotificationDto;
import com.team.playmatebackend.domain.sse.service.SseService;
import org.springframework.stereotype.Service;

/**
 * 알림 생성과 SseService 호출, 비즈니스 로직 집중
 *
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

@Service
public class MatchNotificationService {

    private final SseService sseService;

    public MatchNotificationService(SseService sseService) {
        this.sseService = sseService;
    }

    public void notifyHostOfJoinRequest(String hostId, String userName, String roomId, String userId) {
        NotificationDto dto = new NotificationDto(
                "ROOM_JOIN_REQUEST",
                userName + "님이 방 " + roomId + "에 입장 요청했습니다.",
                roomId,
                userId
        );
        sseService.sendEvent(hostId, dto);
    }

    public void notifyHostOfJoin(String hostId, String userName, String roomId, String userId) {
        NotificationDto dto = new NotificationDto(
                "ROOM_JOINED",
                userName + "님이 방 " + roomId + "에 입장했습니다.",
                roomId,
                userId
        );
        sseService.sendEvent(hostId, dto);
    }

    public void notifyUserOfApproval(String userId, String roomId) {
        NotificationDto dto = new NotificationDto(
                "ROOM_JOIN_APPROVED",
                "방 " + roomId + "에 입장 허가되었습니다.",
                roomId,
                userId
        );
        sseService.sendEvent(userId, dto);
    }
}
