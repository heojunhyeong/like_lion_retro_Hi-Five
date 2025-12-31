import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./PasswordReset.css";
import { requestPasswordReset } from "../api/userApi";

function PasswordReset() {
    const [resetEmail, setResetEmail] = useState("");
    const [resetError, setResetError] = useState("");
    const [resetSuccess, setResetSuccess] = useState(false);
    const navigate = useNavigate();

    const handlePasswordReset = async (e) => {
        e.preventDefault();
        setResetError("");
        setResetSuccess(false);

        try {
            await requestPasswordReset(resetEmail);
            setResetSuccess(true);
            setResetEmail("");
            setTimeout(() => {
                setResetSuccess(false);
                navigate("/login");
            }, 3000);
        } catch (error) {
            console.error("비밀번호 재설정 요청 실패:", error);
            setResetError(error.message || "비밀번호 재설정 요청에 실패했습니다.");
        }
    };

    return (
        <div className="password-reset-page-wrapper">
            <div className="password-reset-container">
                <h2>비밀번호 찾기</h2>
                <form onSubmit={handlePasswordReset}>
                    <div>
                        <label>이메일</label><br />
                        <input
                            type="email"
                            value={resetEmail}
                            onChange={(e) => setResetEmail(e.target.value)}
                            required
                            placeholder="가입하신 이메일을 입력하세요"
                        />
                    </div>

                    {resetError && <div className="error-message">{resetError}</div>}
                    {resetSuccess && (
                        <div className="success-message">
                            비밀번호 재설정 링크가 이메일로 전송되었습니다.
                            <br />
                            3초 후 로그인 페이지로 이동합니다.
                        </div>
                    )}

                    <button type="submit" className="reset-button">
                        재설정 링크 전송
                    </button>

                    <button 
                        type="button" 
                        onClick={() => navigate('/login')}
                        className="back-to-login-button"
                    >
                        로그인으로 돌아가기
                    </button>
                </form>
            </div>
        </div>
    );
}

export default PasswordReset;


