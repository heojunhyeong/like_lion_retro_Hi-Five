// API 기본 URL 설정 (백엔드 서버 주소)
// 환경 변수 VITE_API_URL이 설정되어 있으면 사용, 없으면 기본값 사용
// 도커 환경: 빈 문자열("") 또는 환경 변수로 설정 → nginx 프록시 사용 (/api)
// 로컬 개발: http://localhost:8080 사용
const API_BASE_URL = import.meta.env.VITE_API_URL !== undefined 
    ? import.meta.env.VITE_API_URL 
    : "http://localhost:8080";
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
