package com.team.playmatebackend.domain.chat.controller;

import com.team.playmatebackend.domain.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    // 클라이언트가 /pub/chat/message 경로로 메세지를 보내면 호출
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
        // 메세지를 /sub/chat/room/{matchId} 경로로 구독 중인 모든 유저에게 전달
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getMatchId(), message);
    }
}
