package com.team.playmatebackend.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE 연결 객체 관리를 위한 EmitterRepository 구현
 *
 * @author 김지번
 * @DateOfCreated 2025-12-30
 * @DateOfEdit 2025-12-30
 */

@Repository
public class EmitterRepository {
    // 동시성 문제를 방지하기 위해 ConcurrentHashMap 사용
    // Key: UserId, Value: SseEmitter
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    public void deleteById(String userId) {
        emitters.remove(userId);
    }

    public SseEmitter get(String userId) {
        return emitters.get(userId);
    }

    // EmitterRepository.java 에 추가
    public Map<String, SseEmitter> getAllEmitters() {
        return emitters; // 현재 저장된 모든 맵 반환
    }
}