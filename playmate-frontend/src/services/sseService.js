/**
 * 작업내용: SSE(Server-Sent Events) 연결 관리 서비스 - 실시간 알림 수신을 위한 SSE 연결 및 이벤트 처리
 * 
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

// API 기본 URL 설정 (백엔드 서버 주소)
const API_BASE_URL = import.meta.env.VITE_API_URL !== undefined 
    ? import.meta.env.VITE_API_URL 
    : "http://localhost:8080";

/**
 * SSE 연결을 관리하는 서비스 클래스
 */
class SseService {
    constructor() {
        this.eventSource = null;
        this.listeners = new Map();
        this.currentUserId = null;
    }

    /**
     * SSE 연결 생성
     * @param {string} userId - 사용자 ID
     * @param {Function} onMessage - 메시지 수신 콜백 함수 (notificationDto를 인자로 받음)
     * @param {Function} onError - 에러 처리 콜백 함수
     */
    connect(userId, onMessage, onError) {
        // 기존 연결이 있으면 종료
        this.disconnect();

        this.currentUserId = userId;

        try {
            // SSE 연결 생성
            const url = `${API_BASE_URL}/sse/connect?clientId=${userId}`;
            this.eventSource = new EventSource(url);

            // 연결 성공 시
            this.eventSource.onopen = () => {
                console.log("SSE 연결 성공:", userId);
            };

            // 메시지 수신 시
            this.eventSource.onmessage = (event) => {
                try {
                    const notificationDto = JSON.parse(event.data);
                    console.log("알림 수신:", notificationDto);
                    
                    if (onMessage) {
                        onMessage(notificationDto);
                    }
                    
                    // 이벤트 타입별 리스너 호출
                    this.listeners.forEach((callback, type) => {
                        if (type === notificationDto.type || type === "*") {
                            callback(notificationDto);
                        }
                    });
                } catch (error) {
                    console.error("알림 파싱 오류:", error);
                }
            };

            // 특정 이벤트 타입별 리스너 등록
            this.eventSource.addEventListener("ROOM_JOIN_REQUEST", (event) => {
                try {
                    const notificationDto = JSON.parse(event.data);
                    if (onMessage) onMessage(notificationDto);
                } catch (error) {
                    console.error("ROOM_JOIN_REQUEST 파싱 오류:", error);
                }
            });

            this.eventSource.addEventListener("ROOM_JOINED", (event) => {
                try {
                    const notificationDto = JSON.parse(event.data);
                    if (onMessage) onMessage(notificationDto);
                } catch (error) {
                    console.error("ROOM_JOINED 파싱 오류:", error);
                }
            });

            this.eventSource.addEventListener("ROOM_JOIN_APPROVED", (event) => {
                try {
                    const notificationDto = JSON.parse(event.data);
                    if (onMessage) onMessage(notificationDto);
                } catch (error) {
                    console.error("ROOM_JOIN_APPROVED 파싱 오류:", error);
                }
            });

            // 에러 발생 시
            this.eventSource.onerror = (error) => {
                console.error("SSE 연결 오류:", error);
                
                if (this.eventSource.readyState === EventSource.CLOSED) {
                    console.log("SSE 연결이 종료되었습니다.");
                }
                
                if (onError) {
                    onError(error);
                }
            };

        } catch (error) {
            console.error("SSE 연결 생성 오류:", error);
            if (onError) {
                onError(error);
            }
        }
    }

    /**
     * 특정 이벤트 타입에 대한 리스너 등록
     * @param {string} eventType - 이벤트 타입 (예: "ROOM_JOIN_REQUEST", "*"는 모든 이벤트)
     * @param {Function} callback - 콜백 함수
     */
    addEventListener(eventType, callback) {
        this.listeners.set(eventType, callback);
    }

    /**
     * 특정 이벤트 타입에 대한 리스너 제거
     * @param {string} eventType - 이벤트 타입
     */
    removeEventListener(eventType) {
        this.listeners.delete(eventType);
    }

    /**
     * SSE 연결 종료
     */
    disconnect() {
        if (this.eventSource) {
            this.eventSource.close();
            this.eventSource = null;
            console.log("SSE 연결 종료");
        }
        this.listeners.clear();
        this.currentUserId = null;
    }

    /**
     * 현재 연결 상태 확인
     * @returns {boolean} 연결 상태
     */
    isConnected() {
        return this.eventSource !== null && this.eventSource.readyState === EventSource.OPEN;
    }

    /**
     * 현재 연결된 사용자 ID 반환
     * @returns {string|null} 사용자 ID
     */
    getCurrentUserId() {
        return this.currentUserId;
    }
}

// 싱글톤 인스턴스 생성
const sseService = new SseService();

export default sseService;

