// API 기본 URL 설정 (백엔드 서버 주소)
const API_BASE_URL = "http://localhost:8080";

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


//
// /**
//  * 로그인 API 호출
//  * @param {string} username - 사용자 아이디
//  * @param {string} password - 사용자 비밀번호
//  * @returns {Promise<string>} JWT 토큰
//  */
// export const login = async (username, password) => {
//     try {
//         const response = await fetch(`${API_BASE_URL}/login`, {
//             method: "POST",
//             headers: {
//                 "Content-Type": "application/json",
//             },
//             body: JSON.stringify({
//                 username: username,
//                 password: password,
//             }),
//         });
//
//         if (!response.ok) {
//             const errorData = await response.text();
//             throw new Error(errorData || "로그인에 실패했습니다.");
//         }
//
//         const token = await response.text(); // 백엔드에서 String으로 반환하므로 text() 사용
//         return token;
//     } catch (error) {
//         console.error("Login error:", error);
//         throw error;
//     }
// };
