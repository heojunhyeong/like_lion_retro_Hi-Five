import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { getToken } from '../api/userApi';

let stompClient = null;

/**
 * author최윤혁
 * @DateOfCreated 2025-12-30
 * @Date0fEdit 2025-12-30
 */
export const connectToChat = (matchId, onMessageReceived) => {
    // JWT 토큰 가져오기
    const token = getToken();
    
    // SockJS를 사용하여 WebSocket 연결 생성 (토큰을 쿼리 파라미터로 전달)
    const socket = new SockJS(`/ws-chat${token ? `?token=${token}` : ''}`);
    
    // STOMP 클라이언트 생성
    stompClient = new Client({
        webSocketFactory: () => socket,
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        debug: (str) => {
            console.log('STOMP: ' + str);
        },
        onConnect: () => {
            console.log('WebSocket 연결 성공');
            // 채팅방 구독
            stompClient.subscribe(`/sub/chat/room/${matchId}`, (message) => {
                try {
                    const chatMessage = JSON.parse(message.body);
                    onMessageReceived(chatMessage);
                } catch (error) {
                    console.error('메시지 파싱 오류:', error);
                }
            });
        },
        onStompError: (frame) => {
            console.error('STOMP 오류:', frame);
        },
        onWebSocketError: (event) => {
            console.error('WebSocket 오류:', event);
        },
    });

    // 연결 활성화
    stompClient.activate();
    
    return stompClient;
};

/**
 * 채팅 메시지 전송
 * @param {Long} matchId - 매칭방 ID
 * @param {string} senderId - 발신자 ID
 * @param {string} content - 메시지 내용
 */
export const sendMessage = (matchId, senderId, content) => {
    if (!stompClient) {
        console.error('STOMP 클라이언트가 초기화되지 않았습니다.');
        return false;
    }
    
    if (!stompClient.connected) {
        console.error('STOMP 클라이언트가 연결되지 않았습니다. 연결 대기 중...');
        // 연결이 완료될 때까지 잠시 대기 후 재시도
        setTimeout(() => {
            if (stompClient && stompClient.connected) {
                const message = {
                    matchId: matchId,
                    senderId: senderId,
                    content: content,
                };
                stompClient.publish({
                    destination: '/pub/chat/message',
                    body: JSON.stringify(message),
                });
            } else {
                console.error('메시지 전송 실패: WebSocket 연결이 없습니다.');
            }
        }, 500);
        return false;
    }
    
    const message = {
        matchId: matchId,
        senderId: senderId,
        content: content,
    };
    
    try {
        stompClient.publish({
            destination: '/pub/chat/message',
            body: JSON.stringify(message),
        });
        console.log('메시지 전송 성공:', message);
        return true;
    } catch (error) {
        console.error('메시지 전송 중 오류 발생:', error);
        return false;
    }
};

/**
 * WebSocket 연결 종료
 */
export const disconnect = () => {
    if (stompClient) {
        stompClient.deactivate();
        stompClient = null;
        console.log('WebSocket 연결 종료');
    }
};


