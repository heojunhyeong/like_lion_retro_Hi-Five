import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { createMatch } from "../api/matchApi";
import { getToken } from "../api/userApi";
import "./CreateRoomPage.css";

// 게임 ID와 카테고리 매핑 (GameRoomPage와 동일하게)
const GAME_CATEGORY_MAP = {
  fc_online: "FC_ONLINE",
  league_of_legends: "League_Of_Legends",
  overwatch: "OVER_WATCH",
  maple_story: "MAPLE_STORY",
};

function CreateRoomPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const gameId = searchParams.get("gameId"); // URL에서 gameId 가져오기

  const [title, setTitle] = useState("");
  const [category, setCategory] = useState("");
  const [maxParticipants, setMaxParticipants] = useState(2);
  const [entryMethod, setEntryMethod] = useState("DIRECT");
  const [genderRestriction, setGenderRestriction] = useState("");
  const [ageRestriction, setAgeRestriction] = useState("");
  const [skillRestriction, setSkillRestriction] = useState("ANY");
  const [hashTag, setHashTag] = useState("");

  // gameId가 있으면 해당 카테고리를 자동으로 설정
  useEffect(() => {
    if (gameId && GAME_CATEGORY_MAP[gameId]) {
      setCategory(GAME_CATEGORY_MAP[gameId]);
    }
  }, [gameId]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    // 로그인 체크
    if (!getToken()) {
      alert("로그인이 필요합니다.");
      navigate("/login");
      return;
    }

    // 선택한 정보 공개 값 또는 null을 설정
    const matchData = {
      title: title,
      category: category || null,
      maxParticipants: maxParticipants,
      entryMethod: entryMethod,
      genderRestriction: genderRestriction || null,
      ageRestriction: ageRestriction || null,
      skillRestriction: skillRestriction || null,
      hashTag: hashTag || null,
    };

    try {
      const response = await createMatch(matchData);
      const matchId = response.data;
      console.log("방 생성 성공! 방 ID:", matchId);
      alert("방이 성공적으로 생성되었습니다!");
      // 방 생성 후 해당 게임 페이지로 리다이렉트
      if (gameId) {
        navigate(`/game/${gameId}`);
      } else {
        // gameId가 없으면 채팅방으로 이동
        navigate(`/chat/${matchId}`);
      }
    } catch (error) {
      console.error("방 생성 실패:", error);
      alert(error.message || "방 생성에 실패했습니다.");
    }
  };

  return (
    <div className="create-room-wrapper">
      <div className="create-room-container">
        <h2>방 생성하기</h2>
        <form onSubmit={handleSubmit}>
          {/* 방 제목 */}
          <div className="form-group">
            <label htmlFor="title">방 제목 *</label>
            <input
              type="text"
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
              placeholder="방 제목을 입력하세요"
            />
          </div>

          {/* 카테고리 */}
          <div className="form-group">
            <label htmlFor="category">카테고리</label>
            <select
              id="category"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
            >
              <option value="">선택 안함</option>
              <option value="FC_ONLINE">FC온라인</option>
              <option value="League_Of_Legends">리그 오브 레전드</option>
              <option value="OVER_WATCH">오버워치</option>
              <option value="MAPLE_STORY">메이플스토리</option>
              <option value="기타/취미">기타/취미</option>
            </select>
          </div>

          {/* 최대 인원수 */}
          <div className="form-group">
            <label htmlFor="maxParticipants">최대 인원수 *</label>
            <input
              type="number"
              id="maxParticipants"
              value={maxParticipants}
              onChange={(e) => setMaxParticipants(parseInt(e.target.value))}
              min="2"
              max="20"
              required
            />
          </div>

          {/* 입장 방식 */}
          <div className="form-group">
            <label htmlFor="entryMethod">입장 방식 *</label>
            <select
              id="entryMethod"
              value={entryMethod}
              onChange={(e) => setEntryMethod(e.target.value)}
              required
            >
              <option value="DIRECT">즉시 입장</option>
              <option value="APPROVAL">방장 승인</option>
            </select>
          </div>

          {/* 성별 제한 */}
          <div className="form-group">
            <label htmlFor="genderRestriction">성별 제한</label>
            <select
              id="genderRestriction"
              value={genderRestriction}
              onChange={(e) => setGenderRestriction(e.target.value)}
            >
              <option value="">제한 없음</option>
              <option value="MALE">남성만</option>
              <option value="FEMALE">여성만</option>
            </select>
          </div>

          {/* 나이 제한 */}
          <div className="form-group">
            <label htmlFor="ageRestriction">나이 제한</label>
            <select
              id="ageRestriction"
              value={ageRestriction}
              onChange={(e) => setAgeRestriction(e.target.value)}
            >
              <option value="">제한 없음</option>
              <option value="TEENS">10대</option>
              <option value="TWENTIES">20대</option>
              <option value="THIRTIES">30대</option>
              <option value="FORTIES">40대</option>
              <option value="FIFTIES_PLUS">50대 이상</option>
            </select>
          </div>

          {/* 실력 제한 */}
          <div className="form-group">
            <label htmlFor="skillRestriction">실력 제한 *</label>
            <select
              id="skillRestriction"
              value={skillRestriction}
              onChange={(e) => setSkillRestriction(e.target.value)}
              required
            >
              <option value="ANY">제한 없음</option>
              <option value="BEGINNER">초보</option>
              <option value="INTERMEDIATE">중급</option>
              <option value="PRO">고급</option>
            </select>
          </div>

          {/* 매칭방 태그 */}
          <div className="form-group">
            <label htmlFor="hashTag">매칭방 태그</label>
            <input
              type="text"
              id="hashTag"
              value={hashTag}
              onChange={(e) => setHashTag(e.target.value)}
              placeholder="#태그를 입력하세요 (예: #친목 #랭크)"
            />
          </div>

          <div className="form-actions">
            <button
              type="button"
              onClick={() => navigate("/")}
              className="cancel-button"
            >
              취소
            </button>
            <button type="submit" className="submit-button">
              방 생성하기
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CreateRoomPage;
