/**
 * 작업내용: 매칭방 알림 관련 API 함수 (입장 요청, 입장 완료, 입장 허가 알림 전송)
 * 
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

// API 기본 URL 설정 (백엔드 서버 주소)
const API_BASE_URL = import.meta.env.VITE_API_URL !== undefined 
    ? import.meta.env.VITE_API_URL 
    : "http://localhost:8080";

import { getAuthHeaders } from "./userApi.js";

/**
 * 매칭방 입장 요청 알림 API (방장에게 알림 전송)
 * @param {string} roomId - 방 ID
 * @param {string} userId - 요청한 유저 ID
 * @param {string} userName - 요청한 유저 이름
 * @param {string} hostId - 방장 ID
 * @returns {Promise<Object>} 응답 데이터
 */
export const requestJoinRoom = async (roomId, userId, userName, hostId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/room/${roomId}/request?userId=${userId}&userName=${encodeURIComponent(userName)}&hostId=${hostId}`, {
            method: "POST",
            headers: getAuthHeaders(),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "입장 요청 알림 전송에 실패했습니다.");
        }

        return await response.text();
    } catch (error) {
        console.error("Request join room error:", error);
        throw error;
    }
};

/**
 * 매칭방 입장 완료 알림 API (방장에게 알림 전송)
 * @param {string} roomId - 방 ID
 * @param {string} userId - 입장한 유저 ID
 * @param {string} userName - 입장한 유저 이름
 * @param {string} hostId - 방장 ID
 * @returns {Promise<Object>} 응답 데이터
 */
export const joinRoom = async (roomId, userId, userName, hostId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/room/${roomId}/join?userId=${userId}&userName=${encodeURIComponent(userName)}&hostId=${hostId}`, {
            method: "POST",
            headers: getAuthHeaders(),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "입장 완료 알림 전송에 실패했습니다.");
        }

        return await response.text();
    } catch (error) {
        console.error("Join room error:", error);
        throw error;
    }
};

/**
 * 매칭방 입장 허가 알림 API (유저에게 알림 전송)
 * @param {string} roomId - 방 ID
 * @param {string} hostId - 방장 ID
 * @param {string} userId - 허가 받을 유저 ID
 * @returns {Promise<Object>} 응답 데이터
 */
export const approveJoinRoom = async (roomId, hostId, userId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/room/${roomId}/approve?hostId=${hostId}&userId=${userId}`, {
            method: "POST",
            headers: getAuthHeaders(),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "입장 허가 알림 전송에 실패했습니다.");
        }

        return await response.text();
    } catch (error) {
        console.error("Approve join room error:", error);
        throw error;
    }
};

