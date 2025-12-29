/**
 * 작업내용: 알림 관리를 위한 커스텀 React 훅 - SSE 연결 및 알림 상태 관리를 위한 훅
 * 
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

import { useEffect, useState, useCallback } from "react";
import sseService from "../services/sseService";

/**
 * 알림 관리를 위한 커스텀 훅
 * @param {string} userId - 현재 사용자 ID
 * @returns {Object} { notifications, clearNotifications, isConnected }
 */
export function useNotifications(userId) {
    const [notifications, setNotifications] = useState([]);
    const [isConnected, setIsConnected] = useState(false);

    useEffect(() => {
        if (!userId) {
            setIsConnected(false);
            return;
        }

        // 알림 수신 핸들러
        const handleNotification = (notificationDto) => {
            const newNotification = {
                id: Date.now() + Math.random(),
                ...notificationDto,
                timestamp: new Date(),
            };

            setNotifications((prev) => [...prev, newNotification]);
        };

        // 에러 핸들러
        const handleError = (error) => {
            console.error("SSE 에러:", error);
            setIsConnected(false);
        };

        // 연결 성공 핸들러
        const handleOpen = () => {
            setIsConnected(true);
        };

        // SSE 연결
        sseService.connect(userId, handleNotification, handleError);
        
        // 연결 상태 체크
        const checkInterval = setInterval(() => {
            setIsConnected(sseService.isConnected());
        }, 1000);

        // 컴포넌트 언마운트 시 정리
        return () => {
            clearInterval(checkInterval);
            sseService.disconnect();
            setIsConnected(false);
        };
    }, [userId]);

    // 알림 제거 함수
    const removeNotification = useCallback((notificationId) => {
        setNotifications((prev) =>
            prev.filter((notif) => notif.id !== notificationId)
        );
    }, []);

    // 모든 알림 제거 함수
    const clearNotifications = useCallback(() => {
        setNotifications([]);
    }, []);

    return {
        notifications,
        removeNotification,
        clearNotifications,
        isConnected,
    };
}

