package com.team.playmatebackend.domain.sse.controller;

import com.team.playmatebackend.domain.sse.service.SseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 클라이언트가 SSE를 구독할 수 있는 HTTP 엔드포인트 추가
 *
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

@RestController
public class SseController {

    private final SseService sseService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping("/sse/connect")
    public SseEmitter connect(@RequestParam String clientId) {
        return sseService.connect(clientId);
    }
}
