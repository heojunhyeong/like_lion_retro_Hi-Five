import { useEffect, useState } from "react";
import { getMyProfile, updateMyProfile } from "../api/userApi";
import { useNavigate } from "react-router-dom";
import "./ProfileEditPage.css";

const ProfileEditPage = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    nickName: "",
    preferCategory: "",
    gender: "",
    age: "",
    introduction: "",
  });

  useEffect(() => {
    const fetchProfile = async () => {
      const data = await getMyProfile();
      setForm({
        nickName: data.nickName,
        preferCategory: data.preferCategory,
        gender: data.gender,
        age: data.age,
        introduction: data.introduction || "",
      });
    };

    fetchProfile();
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    try {
      await updateMyProfile(form);
      alert("프로필이 수정되었습니다.");
      navigate("/profile");
    } catch (e) {
      alert(e.message);
    }
  };

  return (
    <div className="profile-edit-card">
      <h2>프로필 수정</h2>

      <label>닉네임</label>
      <input name="nickName" value={form.nickName} onChange={handleChange} />

      <label>선호 게임</label>
      <select
        name="preferCategory"
        value={form.preferCategory}
        onChange={handleChange}
      >
        <option value="LEAGUE_OF_LEGENDS">리그오브레전드</option>
        <option value="OVER_WATCH">오버워치</option>
        <option value="MAPLE_STORY">메이플스토리</option>
        <option value="FC_ONLINE">FC 온라인</option>
        <option value="OTHER">기타</option>
      </select>

      <label>소개</label>
      <textarea
        name="introduction"
        value={form.introduction}
        onChange={handleChange}
      />

      <div className="edit-button-group">
        <button className="cancel-btn" onClick={() => navigate("/profile")}>
          돌아가기
        </button>
        <button className="save-btn" onClick={handleSubmit}>
          수정하기
        </button>
      </div>
    </div>
  );
};

export default ProfileEditPage;
