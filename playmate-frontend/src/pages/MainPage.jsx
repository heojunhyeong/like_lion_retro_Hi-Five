import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getToken, removeToken } from "../api/userApi";
import "./MainPage.css";

function MainPage() {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    // 토큰이 있으면 로그인 상태
    const token = getToken();
    setIsLoggedIn(!!token);
  }, []);

  const handleLoginClick = () => {
    navigate("/login");
  };

  const handleRegisterClick = () => {
    navigate("/register");
  };

  const handleLogoutClick = () => {
    removeToken();
    setIsLoggedIn(false);
  };

  const handleProfileClick = () => {
    navigate("/profile");
  };

  const handleCreateRoom = () => {
    // 로그인 체크
    if (!getToken()) {
      alert("로그인이 필요합니다.");
      navigate("/login");
      return;
    }
    navigate("/createroom");
  };

  const handleJoinRoom = () => {
    // TODO: 방 참여 기능 구현
    navigate("/choosegame");
  };

  return (
    <div className="main-page">
      {/* 헤더 */}
      <header className="main-header">
        <div className="header-content">
          <div className="header-left">
            <div className="logo">PlayMate</div>
          </div>
          <div className="header-right">
            {isLoggedIn ? (
              <>
                <button
                  onClick={handleProfileClick}
                  className="profile-header-button"
                >
                  프로필
                </button>

                <button
                  onClick={handleLogoutClick}
                  className="logout-header-button"
                >
                  로그아웃
                </button>
              </>
            ) : (
              <>
                <button
                  onClick={handleRegisterClick}
                  className="register-header-button"
                >
                  회원가입
                </button>
                <button
                  onClick={handleLoginClick}
                  className="login-header-button"
                >
                  로그인
                </button>
              </>
            )}
          </div>
        </div>
      </header>

      {/* 메인 컨텐츠 */}
      <main className="main-content">
        {/* 사이트 소개 섹션 */}
        <div className="intro-section">
          <div className="intro-box">
            <h2 className="intro-title">사이트 소개 및 설명</h2>
          </div>
        </div>

        {/* 방 생성/참여 섹션 */}
        <div className="room-section">
          <button
            onClick={handleCreateRoom}
            className="room-button create-room-button"
          >
            방 생성하기
          </button>
          <button
            onClick={handleJoinRoom}
            className="room-button join-room-button"
          >
            방 참여하기
          </button>
        </div>
      </main>
    </div>
  );
}

export default MainPage;
