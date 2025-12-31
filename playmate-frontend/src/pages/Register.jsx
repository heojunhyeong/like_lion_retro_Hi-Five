import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Register.css";
import { register } from "../api/userApi";

function Register() {
  const navigate = useNavigate();
  const [userId, setUserId] = useState("");
  const [userPassword, setUserPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [userEmail, setUserEmail] = useState("");
  const [nickName, setNickName] = useState("");
  const [preferCategory, setPreferCategory] = useState("");
  const [gender, setGender] = useState("");
  const [age, setAge] = useState("");
  // const [roleType, setRoleType] = useState(""); //권한 넣을때 사용

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (userPassword !== confirmPassword) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    //React에서 백엔드로 보내는 JSON 요청 형태 Dto UserCreateRequest 와 동일해야함
    const userData = {
      userId: userId,
      password: userPassword,
      confirmPassword: confirmPassword,
      email: userEmail,
      nickname: nickName,
      preferCategory: preferCategory,
      gender: gender,
      age: age,
    };

    try {
      const createdUserId = await register(userData);
      console.log("회원가입 성공! 사용자 ID:", createdUserId);
      alert("회원가입이 성공했습니다!");
      // 알림 확인 버튼 클릭 후 로그인 페이지로 이동
      navigate("/login");
    } catch (error) {
      console.error("회원가입 실패:", error);
      alert(error.message || "회원가입에 실패했습니다.");
    }
  };

  return (
    <div className="register-page-wrapper">
      <div className="register-container">
        <h2>회원가입</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label>ID 입력.</label>
            <br />
            <input
              type="text"
              value={userId}
              onChange={(e) => setUserId(e.target.value)}
              required
            />
          </div>

          <div>
            <label>Password:</label>
            <br />
            <input
              type="password"
              value={userPassword}
              onChange={(e) => setUserPassword(e.target.value)}
              required
            />
          </div>

          <div>
            <label>Password 확인:</label>
            <br />
            <input
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </div>

          <div>
            <label>Email:</label>
            <br />
            <input
              type="email"
              value={userEmail}
              onChange={(e) => setUserEmail(e.target.value)}
              required
            />
          </div>

          <div>
            <label>닉네임 입력</label>
            <br />
            <input
              type="text"
              value={nickName}
              onChange={(e) => setNickName(e.target.value)}
              required
            />
          </div>

          <div>
            <label>선호게임</label>
            <br />
            <select
              value={preferCategory}
              onChange={(e) => setPreferCategory(e.target.value)}
              required
            >
              <option value="">Select</option>
              <option value="LEAGUE_OF_LEGENDS">league of legends</option>
              <option value="FC_ONLINE">피파온라인4</option>
              <option value="OVER_WATCH">오버워치</option>
              <option value="MAPLE_STORY">메이플스토리</option>
            </select>
          </div>

          <div>
            <label>성별</label>
            <br />
            <select
              value={gender}
              onChange={(e) => setGender(e.target.value)}
              required
            >
              <option value="">---</option>
              <option value="MALE">남자</option>
              <option value="FEMALE">여자</option>
            </select>
          </div>

          <div>
            <label>나이</label>
            <br />
            <select
              value={age}
              onChange={(e) => setAge(e.target.value)}
              required
            >
              <option value="">선택하세요</option>
              <option value="TEENS">10대</option>
              <option value="TWENTIES">20대</option>
              <option value="THIRTIES">30대</option>
              <option value="FORTIES">40대</option>
              <option value="FIFTIES_PLUS">50대 이상</option>
            </select>
          </div>

          {/*<div>*/}
          {/*    <label>User Role:</label><br />*/}
          {/*    <select*/}
          {/*        value={roleType}*/}
          {/*        onChange={(e) => setRoleType(e.target.value)}*/}
          {/*        required*/}
          {/*    >*/}
          {/*        <option value="">Select</option>*/}
          {/*        <option value="USER">USER</option>*/}
          {/*        <option value="ADMIN">ADMIN</option>*/}
          {/*        /!* 필요한 역할 추가 *!/*/}
          {/*    </select>*/}
          {/*</div>*/}

          <button type="submit">회원가입</button>
        </form>
      </div>
    </div>
  );
}

export default Register;
