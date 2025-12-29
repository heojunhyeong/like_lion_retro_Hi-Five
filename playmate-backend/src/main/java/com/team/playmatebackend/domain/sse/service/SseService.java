package com.team.playmatebackend.domain.sse.service;

import com.team.playmatebackend.domain.notification.dto.NotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE 이벤트 전송 및 클라이언트 관리 서비스 구현
 *
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

@Service
public class SseService {

    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SseEmitter connect(String clientId) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);
        emitters.put(clientId, emitter);

        emitter.onCompletion(() -> emitters.remove(clientId));
        emitter.onTimeout(() -> emitters.remove(clientId));

        return emitter;
    }

    public void sendEvent(String clientId, NotificationDto notificationDto) {
        SseEmitter emitter = emitters.get(clientId);
        if (emitter != null) {
            try {
                String json = objectMapper.writeValueAsString(notificationDto);
                emitter.send(SseEmitter.event()
                        .name(notificationDto.getType())
                        .data(json));
            } catch (Exception e) {
                emitters.remove(clientId);
            }
        }
    }
}
