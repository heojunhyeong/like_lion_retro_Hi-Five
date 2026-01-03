import { useEffect, useState } from "react";
import { getMyProfile, removeToken } from "../api/userApi";
import { useNavigate } from "react-router-dom";
import "./ProfilePage.css";

const ProfilePage = () => {
  const [profile, setProfile] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await getMyProfile();
        setProfile(data);
      } catch (err) {
        removeToken();
        navigate("/login");
      }
    };

    fetchProfile();
  }, [navigate]);

  if (!profile) {
    return <p>프로필 불러오는 중...</p>;
  }

  return (
    <div className="profile-card">
      <h2 className="profile-title">내 프로필</h2>

      <div className="profile-item">
        <span>ID</span>
        <span>{profile.userId}</span>
      </div>

      <div className="profile-item">
        <span>이메일</span>
        <span>{profile.userEmail}</span>
      </div>

      <div className="profile-item">
        <span>닉네임</span>
        <span>{profile.nickName}</span>
      </div>

      <div className="profile-item">
        <span>선호 게임</span>
        <span>{profile.preferCategory}</span>
      </div>

      <div className="profile-item">
        <span>성별</span>
        <span>{profile.gender}</span>
      </div>

      <div className="profile-item">
        <span>나이</span>
        <span>{profile.age}</span>
      </div>

      {profile.introduction && (
        <div className="profile-intro">
          <span>소개</span>
          <p>{profile.introduction}</p>
        </div>
      )}

      <div className="profile-row">
        <span className="profile-label">비밀번호</span>
        <span className="profile-value">********</span>
        <button
          className="edit-btn"
          onClick={() => navigate("/profile/password")}
        >
          비밀번호 수정
        </button>
      </div>

      <div className="profile-button-group">
        <button
          className="profile-back-button"
          onClick={() => (window.location.href = "http://localhost:70")}
        >
          돌아가기
        </button>

        <button
          className="profile-edit-button"
          onClick={() => navigate("/profile/edit")}
        >
          수정하기
        </button>
      </div>
    </div>
  );
};

export default ProfilePage;
