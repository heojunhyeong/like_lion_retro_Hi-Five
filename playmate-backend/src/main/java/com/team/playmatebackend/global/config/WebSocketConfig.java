package com.team.playmatebackend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 채팅 Config 클래스
 * STOMP 기반 메시지 브로커 방식으로 작성
 *
 *
 * @author 허준형
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 웹소켓 접속 주소를 등록하는 메서드
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 채팅 연결 엔드포인트 : /ws-chat
        registry.addEndpoint("/ws-chat")
                // 리액트 주소 및 도커 환경
                .setAllowedOrigins("http://localhost:5173", "http://localhost:70")
                // 구형 브라우저 또는 일부 모바일 환경 등 WebSocket 연결이 막히는 경우 대체됨
                .withSockJS();
    }

    // 메시지 흐름 규칙을 정하는 메서드
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독, 메시지를 받을 때 사용되는 로직, /sub로 시작하는 경로를 구독하면 메시지를 전달받음
        registry.enableSimpleBroker("/sub");
        // 발행, 메시지를 보낼 때 사용되는 로직
        // 스프링이 내부적으로 매핑해서 @MassageMapping("/chat/message")로 보내줌
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
