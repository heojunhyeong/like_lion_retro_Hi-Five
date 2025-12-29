package com.team.playmatebackend.domain.matching.controller;

import com.team.playmatebackend.domain.notification.service.MatchNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 매칭방 관련 요청 처리 및 SSE 알림 트리거 API 추가
 *
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

@RestController
@RequestMapping("/room")
public class RoomController {

    private final MatchNotificationService notificationService;

    public RoomController(MatchNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/{roomId}/request")
    public ResponseEntity<String> requestJoin(
            @PathVariable String roomId,
            @RequestParam String userId,
            @RequestParam String userName,
            @RequestParam String hostId
    ) {
        notificationService.notifyHostOfJoinRequest(hostId, userName, roomId, userId);
        return ResponseEntity.ok("방장에게 입장 요청 알림 전송 완료");
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<String> joinRoom(
            @PathVariable String roomId,
            @RequestParam String userId,
            @RequestParam String userName,
            @RequestParam String hostId
    ) {
        notificationService.notifyHostOfJoin(hostId, userName, roomId, userId);
        return ResponseEntity.ok("방장에게 입장 완료 알림 전송 완료");
    }

    @PostMapping("/{roomId}/approve")
    public ResponseEntity<String> approveJoin(
            @PathVariable String roomId,
            @RequestParam String hostId,
            @RequestParam String userId
    ) {
        notificationService.notifyUserOfApproval(userId, roomId);
        return ResponseEntity.ok("유저에게 승인 알림 전송 완료");
    }
}
