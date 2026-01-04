package com.team.playmatebackend.domain.notification.controller;

import com.team.playmatebackend.domain.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE 알림 구독을 위한 API 엔드포인트 구현
 *
 * @author 김지번
 * @DateOfCreated 2025-12-30
 * @DateOfEdit 2026-01-04
 */

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // MIME TYPE은 반드시 text/event-stream 이어야 함
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(Authentication authentication, HttpServletResponse response) {
        // [추가] Nginx 등 프록시 서버의 버퍼링 방지
        response.setHeader("X-Accel-Buffering", "no");

        return notificationService.subscribe(authentication.getName());
    }
}