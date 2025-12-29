/**
 * 작업내용: 알림 컨테이너 컴포넌트 - SSE 연결을 관리하고 알림 목록을 표시하는 컨테이너 컴포넌트
 * 
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

import { useEffect, useState } from "react";
import NotificationToast from "./NotificationToast";
import sseService from "../services/sseService";
import "./NotificationToast.css";

/**
 * 알림 컨테이너 컴포넌트
 * SSE 연결을 관리하고 알림을 표시합니다.
 * @param {string} userId - 현재 사용자 ID
 */
function NotificationContainer({ userId }) {
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        if (!userId) return;

        // 알림 수신 핸들러
        const handleNotification = (notificationDto) => {
            const newNotification = {
                id: Date.now() + Math.random(), // 고유 ID 생성
                ...notificationDto,
                timestamp: new Date(),
            };

            setNotifications((prev) => [...prev, newNotification]);
        };

        // 에러 핸들러
        const handleError = (error) => {
            console.error("SSE 에러:", error);
        };

        // SSE 연결
        sseService.connect(userId, handleNotification, handleError);

        // 컴포넌트 언마운트 시 연결 종료
        return () => {
            sseService.disconnect();
        };
    }, [userId]);

    // 알림 제거 함수
    const removeNotification = (notificationId) => {
        setNotifications((prev) =>
            prev.filter((notif) => notif.id !== notificationId)
        );
    };

    return (
        <div className="notification-container">
            {notifications.map((notification) => (
                <NotificationToast
                    key={notification.id}
                    notification={notification}
                    onClose={() => removeNotification(notification.id)}
                    duration={5000}
                />
            ))}
        </div>
    );
}

export default NotificationContainer;

