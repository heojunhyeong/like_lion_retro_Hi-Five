import { useEffect } from 'react';
import { getToken } from '../api/userApi';

const NotificationListener = () => {
    useEffect(() => {
        const token = getToken();
        if (!token) return;

        // 1. SSE 연결 (백엔드 subscribe API 호출)
        const eventSource = new EventSource(`/api/notifications/subscribe`);

        // 2. 브라우저 알림 권한 요청
        if (Notification.permission !== "granted") {
            Notification.requestPermission();
        }

        // 3. 알림 발생 시 처리 함수
        const handleNotification = (event) => {
            try {
                const data = JSON.parse(event.data);

                /**
                 * 이벤트 타입에 따라 알림 제목 설정 추가
                 *
                 * @author 김지번
                 * @DateOfCreated 2025-12-31
                 * @DateOfEdit 2025-12-31
                 */

                // 이벤트 타입(MATCH_ENTER 등)에 따라 제목 설정
                let title = "PlayMate 알림";
                if (event.type === 'MATCH_REQUEST') title = "📩 입장 신청";
                else if (event.type === 'MATCH_ENTER') title = "🎮 멤버 입장";
                else if (event.type === 'MATCH_APPROVED') title = "✅ 입장 승인";
                else if (event.type === 'MATCH_REJECTED') title = "🚫 입장 거절";

                if (Notification.permission === "granted") {
                    const notification = new Notification(title, {
                        body: data.message,
                        icon: '/images/logo.png', // 실제 파일 경로 확인 필요
                        tag: event.type // 같은 타입의 알림은 덮어씌워 도배 방지
                    });

                    notification.onclick = () => {
                        // URL 데이터가 있고 빈 문자열이 아닐 때만 이동 (거절 시에는 이동 안 함)
                        if (data.url && data.url.trim() !== "") {
                            window.location.href = data.url;
                        }

                        window.focus();    // 브라우저 탭 활성화
                        notification.close(); // 클릭 시 알림 닫기
                    };
                }
            } catch (e) {
                console.log("데이터 파싱 오류 또는 더미 데이터:", event.data);
            }
        };

        // 4. 백엔드 이벤트 타입별 리스너 등록
        eventSource.addEventListener('MATCH_ENTER', handleNotification);
        eventSource.addEventListener('MATCH_REQUEST', handleNotification);
        eventSource.addEventListener('MATCH_APPROVED', handleNotification);
        eventSource.addEventListener('MATCH_REJECTED', handleNotification); // 거절 리스너 추가

        // 연결 성공 확인용
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