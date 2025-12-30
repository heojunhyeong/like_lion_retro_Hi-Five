package com.team.playmatebackend.domain.notification.service;

import com.team.playmatebackend.domain.notification.entity.NotificationType;
import com.team.playmatebackend.domain.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * 알림 구독 및 실시간 데이터 전송 서비스 구현
 *
 * @author 김지번
 * @DateOfCreated 2025-12-30
 * @DateOfEdit 2025-12-30
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmitterRepository emitterRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 60분

    /**
     * SSE 연결 구독
     */
    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        // 연결 만료 및 완료 시 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        // 503 에러 방지를 위한 더미 이벤트 전송
        sendToClient(emitter, userId, "EventStream Created. [userId=" + userId + "]");

        return emitter;
    }

    /**
     * 알림 전송 (외부에서 호출)
     */
    public void send(String receiverId, NotificationType type, String content, String url) {
        SseEmitter emitter = emitterRepository.get(receiverId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(receiverId)
                        .name(type.name()) // 클라이언트에서 리스닝할 이벤트 이름
                        .data(new NotificationResponse(type, content, url)));
            } catch (IOException e) {
                emitterRepository.deleteById(receiverId);
                log.error("SSE 연결 오류 발생: {}", e.getMessage());
            }
        }
    }

    // 내부 전송 로직
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류");
        }
    }

    // 알림 응답 DTO (Inner Class로 간단히 정의)
    @lombok.Getter
    @lombok.AllArgsConstructor
    public static class NotificationResponse {
        private NotificationType type;
        private String message;
        private String url; // 클릭 시 이동할 링크
    }
}