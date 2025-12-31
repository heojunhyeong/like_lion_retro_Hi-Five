import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAuthHeaders, removeToken } from "../api/userApi";
import "./ProfilePasswordPage.css";

function ProfilePasswordPage() {
  const navigate = useNavigate();

  const [currentPassword, setCurrentPassword] = useState("");
  const [verified, setVerified] = useState(false);
  const [error, setError] = useState("");

  const [newPassword, setNewPassword] = useState("");
  const [newPasswordConfirm, setNewPasswordConfirm] = useState("");

  const verifyPassword = async () => {
    if (!currentPassword) {
      setError("현재 비밀번호를 입력해주세요.");
      return;
    }

    try {
      const response = await fetch("/api/users/me/password/verify", {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify({ password: currentPassword }),
      });

      if (!response.ok) throw new Error();

      setVerified(true);
      setError("");
    } catch {
      setError("비밀번호가 일치하지 않습니다.");
      setVerified(false);
    }
  };

  const changePassword = async () => {
    if (newPassword !== newPasswordConfirm) {
      setError("새 비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      const response = await fetch("/api/users/me/password", {
        method: "PUT",
        headers: getAuthHeaders(),
        body: JSON.stringify({
          currentPassword,
          newPassword,
          newPasswordConfirm,
        }),
      });

      if (!response.ok) throw new Error();

      alert("비밀번호가 변경되었습니다.\n다시 로그인해주세요.");
      removeToken();
      navigate("/login");
    } catch {
      setError("비밀번호 변경에 실패했습니다.");
    }
  };

  return (
    <div className="password-page-wrapper">
      <div className="password-page">
        <h2>비밀번호 변경</h2>

        <input
          type="password"
          placeholder="현재 비밀번호"
          value={currentPassword}
          onChange={(e) => setCurrentPassword(e.target.value)}
        />

        <button onClick={verifyPassword}>비밀번호 확인</button>

        {error && <p className="error">{error}</p>}

        {verified && (
          <>
            <input
              type="password"
              placeholder="새 비밀번호"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />

            <input
              type="password"
              placeholder="새 비밀번호 확인"
              value={newPasswordConfirm}
              onChange={(e) => setNewPasswordConfirm(e.target.value)}
            />

            <button onClick={changePassword}>비밀번호 변경</button>
          </>
        )}

        <button className="back-button" onClick={() => navigate("/profile")}>
          돌아가기
        </button>
      </div>
    </div>
  );
}

export default ProfilePasswordPage;
