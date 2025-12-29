// API 기본 URL 설정 (백엔드 서버 주소)
// 도커 환경에서는 nginx를 통해 /api/로 프록시되므로 빈 문자열 사용
const API_BASE_URL = "";
/**
 * 회원가입 API 호출 (필요시 사용)
 * @param {Object} userData - 회원가입 데이터
 * @returns {Promise<Object>} 응답 데이터
 */
export const register = async (userData) => {
    try {
        const response = await fetch(`${API_BASE_URL}/api/users/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(userData),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "회원가입에 실패했습니다.");
        }

        return await response.json();
    } catch (error) {
        console.error("Register error:", error);
        throw error;
    }
};


/**
 * 로그인 API 호출
 * @param {string} userID - 사용자 아이디
 * @param {string} userPassword - 사용자 비밀번호
 * @returns {Promise<Object>} { accessToken, refreshToken }
 */
export const login = async (userID, userPassword) => {
    try {
        const response = await fetch(`${API_BASE_URL}/api/users/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                userID: userID,
                userPassword: userPassword,
            }),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "로그인에 실패했습니다.");
        }

        const data = await response.json();
        
        // Access Token과 Refresh Token 저장
        localStorage.setItem("accessToken", data.accessToken);
        localStorage.setItem("refreshToken", data.refreshToken);
        // 사용자 ID 저장 (매칭방 알림 기능을 위해 추가 - 로그인에 사용한 userID)
        localStorage.setItem("userId", userID);
        
        return data;
    } catch (error) {
        console.error("Login error:", error);
        throw error;
    }
};

/**
 * JWT 토큰을 가져오는 헬퍼 함수
 * @returns {string|null} localStorage에 저장된 Access Token
 */
export const getToken = () => {
    return localStorage.getItem("accessToken");
};

/**
 * JWT 토큰을 제거하는 헬퍼 함수 (로그아웃 시 사용)
 */
export const removeToken = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("userId");
};

/**
 * 사용자 ID를 저장하는 함수
 * 
 * 작업내용: 매칭방 알림 기능을 위해 사용자 ID 저장 기능 추가
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 * 
 * @param {string} userId - 사용자 ID
 */
export const setUserId = (userId) => {
    localStorage.setItem("userId", userId);
};

/**
 * 저장된 사용자 ID를 가져오는 함수
 * 
 * 작업내용: 매칭방 알림 기능을 위해 사용자 ID 조회 기능 추가
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 * 
 * @returns {string|null} localStorage에 저장된 사용자 ID
 */
export const getUserId = () => {
    return localStorage.getItem("userId");
};

/**
 * 인증이 필요한 API 요청을 위한 헤더 생성
 * @returns {Object} Authorization 헤더가 포함된 headers 객체
 */
export const getAuthHeaders = () => {
    const token = getToken();
    return {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
    };
};

/**
 * 현재 로그인한 사용자의 프로필 정보 조회
 * @returns {Promise<Object>} 사용자 프로필 정보
 */
export const getMyProfile = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/api/users/me`, {
            method: "GET",
            headers: getAuthHeaders(),
        });

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || "프로필 조회에 실패했습니다.");
        }

        return await response.json();
    } catch (error) {
        console.error("Get profile error:", error);
        throw error;
    }
};
