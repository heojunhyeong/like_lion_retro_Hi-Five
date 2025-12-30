package com.team.playmatebackend.domain.chat.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private Long matchId; // 어느 매칭방의 채팅인가
    private String senderId; // 누가 보냈는가
    private String content; // 내용
}
