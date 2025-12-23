import { useState } from "react";
import "./Register.css";
import { register } from "../api/userApi";

function UserForm() {
    const [userId, setUserId] = useState("");
    const [userPassword, setUserPassword] = useState("");
    const [userEmail, setUserEmail] = useState("");
    const [nickName, setNickName] = useState("");
    const [preferCategory, setPreferCategory] = useState("");
    const [gender, setGender] = useState("");
    const [age, setAge] = useState("");
    // const [roleType, setRoleType] = useState(""); //권한 넣을때 사용

    const handleSubmit = async (e) => {
        e.preventDefault();

        const userData = {
            userId,
            userPassword,
            userEmail,
            nickName,
            preferCategory,
            gender,
            age,
            //roleType,
        };

        try {
            const createdUserId = await register(userData);
            console.log("회원가입 성공! 사용자 ID:", createdUserId);
            alert("회원가입이 완료되었습니다!");
            // 회원가입 성공 후 폼 초기화 또는 페이지 이동
        } catch (error) {
            console.error("회원가입 실패:", error);
            alert(error.message || "회원가입에 실패했습니다.");
        }
    };

    return (
        <div>
            <h2>회원가입</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>ID 입력.</label><br />
                    <input
                        type="text"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Password:</label><br />
                    <input
                        type="password"
                        value={userPassword}
                        onChange={(e) => setUserPassword(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Email:</label><br />
                    <input
                        type="email"
                        value={userEmail}
                        onChange={(e) => setUserEmail(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>닉네임 입력</label><br />
                    <input
                        type="text"
                        value={nickName}
                        onChange={(e) => setNickName(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>선호게임</label><br />
                    <select
                        value={preferCategory}
                        onChange={(e) => setPreferCategory(e.target.value)}
                        required
                    >
                        <option value="">Select</option>
                        <option value="league of legends">league of legends</option>
                        <option value="피파온라인4">피파온라인4</option>
                        <option value="오버워치">오버워치</option>
                        <option value="메이플스토리">메이플스토리</option>
                    </select>
                </div>

                <div>
                    <label>성별</label><br />
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
                    <label>나이:</label><br />
                    <input
                        type="text"
                        value={age}
                        onChange={(e) => setAge(e.target.value)}
                        required
                    />
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
    );
}

export default UserForm;