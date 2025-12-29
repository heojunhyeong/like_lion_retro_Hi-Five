import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";
import { login } from "../api/userApi";

function Login() {
    const [userID, setUserID] = useState("");
    const [userPassword, setUserPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        try {
            // 로그인 API 호출 (LoginResponseDto 반환: { accessToken, refreshToken })
            const response = await login(userID, userPassword);
            
            // 토큰은 userApi.js에서 이미 로컬에 저장됨
            console.log("로그인 성공!");
            
            // 로그인 성공 하면 메인 페이지로 이동
            navigate("/");
        } catch (error) {
            console.error("로그인 실패:", error);
            setError("아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해주세요!");
        }
    };

    return (
        <div className="login-container">
            <h2>로그인</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>아이디</label><br />
                    <input
                        type="text"
                        value={userID}
                        onChange={(e) => setUserID(e.target.value)}
                        required
                        placeholder="아이디를 입력하세요"
                    />
                </div>

                <div>
                    <label>비밀번호</label><br />
                    <input
                        type="password"
                        value={userPassword}
                        onChange={(e) => setUserPassword(e.target.value)}
                        required
                        placeholder="비밀번호를 입력하세요"
                    />
                </div>

                {error && <div className="error-message">{error}</div>}

                <div className="button-container">
                    <button type="submit">로그인</button>
                    <button 
                        type="button" 
                        onClick={() => navigate('/register')}
                        className="register-button"
                    >
                        회원가입
                    </button>
                </div>
            </form>
        </div>
    );
}

export default Login;

