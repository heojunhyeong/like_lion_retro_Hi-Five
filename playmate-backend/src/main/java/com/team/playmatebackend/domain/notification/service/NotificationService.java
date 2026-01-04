package com.team.playmatebackend.domain.notification.service;

import com.team.playmatebackend.domain.notification.entity.NotificationType;
import com.team.playmatebackend.domain.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

/**
 * 알림 구독 및 실시간 데이터 전송 서비스 구현
 * 0104 하트비트 전송 메서드 추가
 *
 * @author 김지번
 * @DateOfCreated 2025-12-30
 * @DateOfEdit 2026-01-04
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmitterRepository emitterRepository;
    // 타임아웃을 1시간으로 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        // 연결 만료 및 완료 시 리포지토리에서 삭제
        emitter.onCompletion(() -> {
            log.info("SSE 연결 완료: userId={}", userId);
            emitterRepository.deleteById(userId);
        });
        emitter.onTimeout(() -> {
            log.warn("SSE 연결 타임아웃: userId={}", userId);
            emitterRepository.deleteById(userId);
        });
        emitter.onError((e) -> {
            log.error("SSE 연결 에러: userId={}, message={}", userId, e.getMessage());
            emitterRepository.deleteById(userId);
        });

        emitterRepository.save(userId, emitter);

        // 1. 첫 연결 시 503 에러 방지를 위한 더미 데이터 전송
        sendToClient(emitter, userId, "EventStream Created. [userId=" + userId + "]");

        return emitter;
    }

    /**
     * 주기적으로 하트비트를 전송하여 연결 유지 (30초 간격)
     * 이 작업을 위해 메인 Application 클래스에 @EnableScheduling 추가 필요
     */
    @Scheduled(fixedRate = 30000)
    public void sendHeartbeat() {
        // EmitterRepository에 접근하여 모든 연결된 사용자에게 빈 데이터 전송
        // (참고: EmitterRepository에 모든 Emitters를 가져오는 메서드 추가 필요)
        Map<String, SseEmitter> allEmitters = emitterRepository.getAllEmitters();

        allEmitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(userId)
                        .name("heartbeat")
                        .data("ping"));
                log.debug("하트비트 전송: userId={}", userId);
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
            }
        });
    }

    public void send(String receiverId, NotificationType type, String content, String url) {
        SseEmitter emitter = emitterRepository.get(receiverId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(receiverId)
                        .name(type.name())
                        .data(new NotificationResponse(type, content, url)));
            } catch (IOException e) {
                emitterRepository.deleteById(receiverId);
                log.error("전송 중 SSE 연결 오류 발생: {}", e.getMessage());
            }
        }
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(id);
        }
    }

    @lombok.Getter
    @lombok.AllArgsConstructor
    public static class NotificationResponse {
        private NotificationType type;
        private String message;
        private String url;
    }
}