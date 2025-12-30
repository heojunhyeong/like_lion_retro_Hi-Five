import { useEffect } from 'react';
import { getToken } from '../api/userApi';

const NotificationListener = () => {
    useEffect(() => {
        const token = getToken();
        if (!token) return;

        // 1. SSE 연결 (백엔드 subscribe API 호출)
        // 주의: 백엔드 컨트롤러에서 userId를 어떻게 추출하는지에 따라
        // URL 뒤에 토큰을 붙이거나 (/api/notifications/subscribe?token=...)
        // 그냥 호출하면 백엔드 SecurityContext에서 처리할 수도 있습니다.
        const eventSource = new EventSource(`/api/notifications/subscribe`);

        // 2. 브라우저 알림 권한 요청
        if (Notification.permission !== "granted") {
            Notification.requestPermission();
        }

        // 3. 알림 발생 시 처리 함수 (백엔드의 NotificationResponse 구조와 일치)
        const handleNotification = (event) => {
            // "EventStream Created" 같은 단순 문자열 더미 데이터 처리용 예외 처리
            try {
                const data = JSON.parse(event.data);

                if (Notification.permission === "granted") {
                    const notification = new Notification("PlayMate 알림", {
                        body: data.message, // NotificationResponse의 message 필드
                        icon: '/favicon.ico'
                    });

                    notification.onclick = () => {
                        if (data.url) window.location.href = data.url;
                        window.focus();
                    };
                }
            } catch (e) {
                console.log("더미 데이터 또는 형식 오류:", event.data);
            }
        };

        // 4. 백엔드 서비스의 type.name()에 해당하는 이벤트 리스너 등록
        // 백엔드 NotificationType Enum의 이름들과 일치해야 합니다.
        eventSource.addEventListener('MATCH_ENTER', handleNotification);
        eventSource.addEventListener('MATCH_REQUEST', handleNotification);
        eventSource.addEventListener('MATCH_APPROVED', handleNotification);

        // 백엔드에서 최초 연결 시 보내는 "sse" 타입 더미 이벤트 처리 (선택사항)
        eventSource.addEventListener('sse', (e) => {
            console.log("SSE 연결 성공:", e.data);
        });

        eventSource.onerror = (err) => {
            console.error("SSE 연결 중 오류 발생:", err);
            eventSource.close();
        };

        return () => {
            eventSource.close();
        };
    }, []);

    return null;
};

export default NotificationListener;