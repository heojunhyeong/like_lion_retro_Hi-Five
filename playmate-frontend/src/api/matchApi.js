import { getAuthHeaders } from "./userApi";

// 도커 환경에서는 nginx를 통해 /api/로 프록시되므로 빈 문자열 사용
const MATCH_API_BASE_URL = "";

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

/**
<<<<<<< Updated upstream
 * 방 목록 조회 API 호출
 * @param {string} category - 게임 카테고리 (PreferCategory enum 값)
 * @returns {Promise<Array>} 방 목록
 */
export const getMatches = async (category = null) => {
  try {
    let url = `${MATCH_API_BASE_URL}/api/matches`;
    const params = new URLSearchParams();

    if (category) {
      params.append("preferCategory", category);
    }

    if (params.toString()) {
      url += `?${params.toString()}`;
    }

    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData || "방 목록 조회에 실패했습니다.");
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Get matches error:", error);
    throw error;
  }
=======
 * 방 상세 정보 조회
 * @param {number} matchId - 방 ID
 * @returns {Promise<Object>} 방 상세 정보
 */
export const getMatchDetail = async (matchId) => {
    try {
        const response = await fetch(`${MATCH_API_BASE_URL}/api/matches/${matchId}`, {
            method: "GET",
            headers: getAuthHeaders(),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "방 정보 조회에 실패했습니다.");
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Get match detail error:", error);
        throw error;
    }
};

/**
 * 참가자 목록 조회
 * @param {number} matchId - 방 ID
 * @returns {Promise<Object>} 참가자 목록
 */
export const getParticipants = async (matchId) => {
    try {
        const response = await fetch(`${MATCH_API_BASE_URL}/api/matches/${matchId}/participants`, {
            method: "GET",
            headers: getAuthHeaders(),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "참가자 목록 조회에 실패했습니다.");
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Get participants error:", error);
        throw error;
    }
};

/**
 * 참가자 승인
 * @param {number} participantId - 참가자 ID
 * @returns {Promise<Object>} 응답 데이터
 */
export const approveParticipant = async (participantId) => {
    try {
        const response = await fetch(`${MATCH_API_BASE_URL}/api/matches/participants/${participantId}/approve`, {
            method: "POST",
            headers: getAuthHeaders(),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "참가자 승인에 실패했습니다.");
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Approve participant error:", error);
        throw error;
    }
};

/**
 * 참가자 거절
 * @param {number} participantId - 참가자 ID
 * @returns {Promise<Object>} 응답 데이터
 */
export const rejectParticipant = async (participantId) => {
    try {
        const response = await fetch(`${MATCH_API_BASE_URL}/api/matches/participants/${participantId}/reject`, {
            method: "POST",
            headers: getAuthHeaders(),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "참가자 거절에 실패했습니다.");
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Reject participant error:", error);
        throw error;
    }
>>>>>>> Stashed changes
};
