/**
 * 작업내용: 알림 토스트 UI 컴포넌트 - 매칭방 알림을 화면에 표시하는 토스트 컴포넌트
 * 
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

import { useEffect, useState } from "react";
import "./NotificationToast.css";

/**
 * 알림 토스트 컴포넌트
 * @param {Object} notification - 알림 데이터 { type, message, roomId, userId }
 * @param {Function} onClose - 닫기 콜백 함수
 * @param {number} duration - 자동 닫기 시간 (ms, 기본값: 5000)
 */
function NotificationToast({ notification, onClose, duration = 5000 }) {
    const [isVisible, setIsVisible] = useState(true);

    useEffect(() => {
        // 자동 닫기 타이머 설정
        const timer = setTimeout(() => {
            setIsVisible(false);
            setTimeout(() => {
                if (onClose) onClose();
            }, 300); // 페이드아웃 애니메이션 시간
        }, duration);

        return () => clearTimeout(timer);
    }, [duration, onClose]);

    if (!notification) return null;

    // 알림 타입에 따른 아이콘 및 스타일 설정
    const getNotificationConfig = (type) => {
        switch (type) {
            case "ROOM_JOIN_REQUEST":
                return {
                    icon: "📥",
                    className: "notification-request",
                    title: "입장 요청"
                };
            case "ROOM_JOINED":
                return {
                    icon: "✅",
                    className: "notification-joined",
                    title: "입장 완료"
                };
            case "ROOM_JOIN_APPROVED":
                return {
                    icon: "🎉",
                    className: "notification-approved",
                    title: "입장 허가"
                };
            default:
                return {
                    icon: "🔔",
                    className: "notification-default",
                    title: "알림"
                };
        }
    };

    const config = getNotificationConfig(notification.type);

    const handleClose = () => {
        setIsVisible(false);
        setTimeout(() => {
            if (onClose) onClose();
        }, 300);
    };

    return (
        <div className={`notification-toast ${config.className} ${isVisible ? "visible" : "hidden"}`}>
            <div className="notification-content">
                <div className="notification-icon">{config.icon}</div>
                <div className="notification-body">
                    <div className="notification-title">{config.title}</div>
                    <div className="notification-message">{notification.message}</div>
                    {notification.roomId && (
                        <div className="notification-room-id">방 ID: {notification.roomId}</div>
                    )}
                </div>
                <button className="notification-close" onClick={handleClose}>
                    ×
                </button>
            </div>
        </div>
    );
}

export default NotificationToast;

