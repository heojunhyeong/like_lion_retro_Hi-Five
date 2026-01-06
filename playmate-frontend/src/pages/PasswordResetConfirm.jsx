import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import "./PasswordReset.css";
import { resetPassword } from "../api/userApi";

function PasswordResetConfirm() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);
    const [token, setToken] = useState("");

    useEffect(() => {
        const tokenParam = searchParams.get("token");
        if (!tokenParam) {
            setError("유효하지 않은 링크입니다.");
        } else {
            setToken(tokenParam);
        }
    }, [searchParams]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setSuccess(false);

        // 토큰 확인
        if (!token) {
            setError("유효하지 않은 링크입니다.");
            return;
        }

        // 비밀번호 확인
        if (newPassword !== confirmPassword) {
            setError("새 비밀번호가 일치하지 않습니다.");
            return;
        }

        // 비밀번호 길이 확인
        if (newPassword.length < 8) {
            setError("비밀번호는 8자 이상이어야 합니다.");
            return;
        }

        try {
            await resetPassword(token, newPassword);
            setSuccess(true);
            setTimeout(() => {
                navigate("/login");
            }, 2000);
        } catch (error) {
            console.error("비밀번호 재설정 실패:", error);
            let errorMessage = "비밀번호 재설정에 실패했습니다.";
            
            if (error.message) {
                if (error.message.includes("만료") || error.message.includes("expired")) {
                    errorMessage = "링크가 만료되었습니다. 다시 비밀번호 찾기를 진행해주세요.";
                } else if (error.message.includes("사용") || error.message.includes("used")) {
                    errorMessage = "이미 사용된 링크입니다. 다시 비밀번호 찾기를 진행해주세요.";
                } else if (error.message.includes("찾을 수 없") || error.message.includes("not found")) {
                    errorMessage = "유효하지 않은 링크입니다. 다시 비밀번호 찾기를 진행해주세요.";
                } else {
                    errorMessage = error.message;
                }
            }
            
            setError(errorMessage);
        }
    };

    return (
        <div className="password-reset-page-wrapper">
            <div className="password-reset-container">
                <h2>비밀번호 재설정</h2>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>새 비밀번호</label><br />
                        <input
                            type="password"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            required
                            placeholder="새 비밀번호를 입력하세요 (8자 이상)"
                            minLength={8}
                        />
                    </div>

                    <div>
                        <label>새 비밀번호 확인</label><br />
                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                            placeholder="새 비밀번호를 다시 입력하세요"
                            minLength={8}
                        />
                    </div>

                    {error && <div className="error-message">{error}</div>}
                    {success && (
                        <div className="success-message">
                            비밀번호가 성공적으로 변경되었습니다.
                            <br />
                            2초 후 로그인 페이지로 이동합니다.
                        </div>
                    )}

                    <button 
                        type="submit" 
                        className="reset-button"
                        disabled={!token || success}
                    >
                        비밀번호 변경
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

export default PasswordResetConfirm;

