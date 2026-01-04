//package com.team.playmatebackend.global.config;
//
//import com.team.playmatebackend.domain.matching.service.MatchService;
//import com.team.playmatebackend.global.exception.CustomException;
//import com.team.playmatebackend.global.exception.ErrorCode;
//import com.team.playmatebackend.global.jwt.JwtProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.stereotype.Component;
//
//
//
//@Component
//@RequiredArgsConstructor
//public class StompHandler implements ChannelInterceptor {
//
//    private final JwtProvider jwtProvider;
//    private final MatchService matchService;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        // 연결(CONNECT) 또는 구독(SUBSCRIBE) 시점에 토큰 및 권한 검증
//        if (StompCommand.CONNECT == accessor.getCommand() || StompCommand.SUBSCRIBE == accessor.getCommand()) {
//
//            // 헤더에서 토큰 추출 (Bearer 제외)
//            String token = extractToken(accessor);
//
//            // 토큰 유효성 검증
//            if (!jwtProvider.validateToken(token)) {
//                throw new IllegalArgumentException("유효하지 않은 토큰입니다");
//            }
//
//            // 토큰에서 userId(username) 추출
//            String userId = jwtProvider.getUsername(token);
//
//            // 구독 요청일 경우, 해당 방에 대한 참여 권한(ACCEPTED)이 있는지 추가 검증
//            if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
//                String destination = accessor.getDestination(); // 예: /sub/chat/room/2
//                Long matchId = parseMatchId(destination);
//
//                // 매칭 서비스의 권한 검증 메서드 호출
//                matchService.checkChatAccess(userId, matchId);
//            }
//        }
//
//        return message;
//    }
//
//    private String extractToken(StompHeaderAccessor accessor) {
//        String bearerToken = accessor.getFirstNativeHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        throw new IllegalArgumentException("토큰을 찾을 수 없습니다");
//    }
//
//    private Long parseMatchId(String destination) {
//        try {
//            if (destination == null || !destination.contains("/")) {
//                throw new IllegalArgumentException();
//            }
//            return Long.parseLong(destination.substring(destination.lastIndexOf("/") + 1));
//        } catch (Exception e) {
//            throw new IllegalArgumentException("매칭방 아이디가 틀립니다");
//        }
//    }
//}