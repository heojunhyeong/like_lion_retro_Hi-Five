import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getToken, removeToken } from "../../api/userApi";
import "./Header.css";

function Header() {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    setIsLoggedIn(!!getToken());
  }, []);

  const handleLogin = () => navigate("/login");
  const handleRegister = () => navigate("/register");
  const handleProfile = () => navigate("/profile");

  const handleLogout = () => {
    removeToken();
    setIsLoggedIn(false);
    navigate("/");
  };

  return (
    <header className="main-header">
      <div className="header-content">
        <div className="header-left">
          <div className="logo" onClick={() => navigate("/")}>
            PlayMate
          </div>
        </div>

        <div className="header-right">
          {isLoggedIn ? (
            <>
              <button className="profile-header-button" onClick={handleProfile}>
                프로필
              </button>
              <button className="logout-header-button" onClick={handleLogout}>
                로그아웃
              </button>
            </>
          ) : (
            <>
              <button
                className="register-header-button"
                onClick={handleRegister}
              >
                회원가입
              </button>
              <button className="login-header-button" onClick={handleLogin}>
                로그인
              </button>
            </>
          )}
        </div>
      </div>
    </header>
  );
}

export default Header;
