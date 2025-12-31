/**
 * JWT 토큰에서 payload를 추출하는 유틸리티
 * @param {string} token - JWT 토큰
 * @returns {Object|null} 디코딩된 payload 객체
 */
export const decodeJWT = (token) => {
    try {
        if (!token) return null;
        
        // JWT는 . 으로 구분된 3부분 (header.payload.signature)
        const base64Url = token.split('.')[1];
        if (!base64Url) return null;
        
        // base64url 디코딩
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        
        return JSON.parse(jsonPayload);
    } catch (error) {
        console.error('JWT 디코딩 오류:', error);
        return null;
    }
};

/**
 * JWT 토큰에서 사용자 ID 추출
 * @returns {string|null} 사용자 ID (subject)
 */
export const getUserIdFromToken = () => {
    const token = localStorage.getItem('accessToken');
    if (!token) return null;
    
    const decoded = decodeJWT(token);
    return decoded?.sub || decoded?.userId || null;
};


