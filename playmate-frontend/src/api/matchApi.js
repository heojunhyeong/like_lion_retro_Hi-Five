import { getAuthHeaders } from './userApi';

// 도커 환경에서는 nginx를 통해 /api/로 프록시되므로 빈 문자열 사용
const MATCH_API_BASE_URL = '';

/**
 * 방 생성 API 호출
 * @param {Object} matchData - 방 생성 데이터
 * @returns {Promise<Object>} 응답 데이터 (matchId)
 */
export const createMatch = async (matchData) => {
    try {
        const response = await fetch(`${MATCH_API_BASE_URL}/api/matches`, {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify(matchData),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "방 생성에 실패했습니다.");
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Create match error:", error);
        throw error;
    }
};

