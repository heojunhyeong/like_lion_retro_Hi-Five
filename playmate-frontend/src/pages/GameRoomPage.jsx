import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getToken } from "../api/userApi";
import { getMatches, applyToMatch } from "../api/matchApi";
import "./GameRoomPage.css";

// 게임 ID와 카테고리 매핑
const GAME_CATEGORY_MAP = {
    fc_online: "FC_ONLINE",
    league_of_legends: "LEAGUE_OF_LEGENDS",
    overwatch: "OVER_WATCH",
    maple_story: "MAPLE_STORY",
    hobby: "OTHER",
};

const GAME_NAME_MAP = {
    fc_online: "FC온라인",
    league_of_legends: "리그오브레전드",
    overwatch: "오버워치",
    maple_story: "메이플스토리",
    hobby: "기타",
};

function GameRoomPage() {
    const navigate = useNavigate();
    const { gameId } = useParams();
    const [rooms, setRooms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [sortType, setSortType] = useState("LATEST");
    const [searchKeyword, setSearchKeyword] = useState("");

    useEffect(() => {
        const loadRooms = async () => {
            try {
                setLoading(true);
                const category = GAME_CATEGORY_MAP[gameId];
                if (!category) {
                    console.error("알 수 없는 게임 ID:", gameId);
                    navigate("/choosegame");
                    return;
                }
                const roomList = await getMatches(category, sortType, null);
                setRooms(roomList);
            } catch (error) {
                console.error("방 목록 로드 실패:", error);
                alert("방 목록을 불러오는데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        if (gameId) {
            loadRooms();
        }
    }, [gameId, sortType, navigate]);

    const handleBackClick = () => {
        navigate("/choosegame");
    };

    const handleRoomClick = async (roomId) => {
        // 로그인 체크
        if (!getToken()) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }

        try {
            // 방 입장 API 호출
            await applyToMatch(roomId);
            
            // 성공하면 채팅방으로 이동
            navigate(`/chat/${roomId}`);
        } catch (error) {
            console.error("방 입장 실패:", error);
            alert(error.message || "방 입장에 실패했습니다.");
        }
    };

    const handleCreateRoom = () => {
        if (!getToken()) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }
        // gameId를 쿼리 파라미터로 전달
        navigate(`/createroom?gameId=${gameId}`);
    };

    const handleSortChange = (newSortType) => {
        setSortType(newSortType);
    };

    const handleSearchChange = (e) => {
        setSearchKeyword(e.target.value);
    };

    const handleSearchSubmit = async (e) => {
        e.preventDefault();
        try {
            setLoading(true);
            const category = GAME_CATEGORY_MAP[gameId];
            if (!category) {
                return;
            }
            // 검색어를 포함하여 검색
            const roomList = await getMatches(category, sortType, searchKeyword);
            setRooms(roomList);
        } catch (error) {
            console.error("방 목록 로드 실패:", error);
            alert("방 목록을 불러오는데 실패했습니다.");
        } finally {
            setLoading(false);
        }
    };

    const handleSearchReset = async () => {
        setSearchKeyword("");
        try {
            setLoading(true);
            const category = GAME_CATEGORY_MAP[gameId];
            if (!category) {
                return;
            }
            const roomList = await getMatches(category, sortType, null);
            setRooms(roomList);
        } catch (error) {
            console.error("방 목록 로드 실패:", error);
            alert("방 목록을 불러오는데 실패했습니다.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="game-room-page">
            {/* 메인 컨텐츠 */}
            <main className="main-content">
                <div className="game-room-header">
                    <button onClick={handleBackClick} className="back-button">
                        ← 뒤로가기
                    </button>
                    <h2 className="game-title">
                        {GAME_NAME_MAP[gameId] || "게임"} 방 목록
                    </h2>
                    <button onClick={handleCreateRoom} className="create-room-button">
                        방 만들기
                    </button>
                </div>

                {/* 검색창 */}
                <div className="search-controls">
                    <form onSubmit={handleSearchSubmit} className="search-form">
                        <input
                            type="text"
                            className="search-input"
                            placeholder="방 제목으로 검색..."
                            value={searchKeyword}
                            onChange={handleSearchChange}
                        />
                        <button type="submit" className="search-button">
                            검색
                        </button>
                        {searchKeyword && (
                            <button
                                type="button"
                                onClick={handleSearchReset}
                                className="reset-button"
                            >
                                초기화
                            </button>
                        )}
                    </form>
                </div>

                {/* 정렬 버튼 */}
                <div className="sort-controls">
                    <span className="sort-label">정렬:</span>
                    <button
                        className={`sort-button ${sortType === "LATEST" ? "active" : ""}`}
                        onClick={() => handleSortChange("LATEST")}
                    >
                        최신순
                    </button>
                    <button
                        className={`sort-button ${sortType === "PARTICIPANTS" ? "active" : ""}`}
                        onClick={() => handleSortChange("PARTICIPANTS")}
                    >
                        인원순
                    </button>
                </div>

                {loading ? (
                    <div className="loading">로딩 중...</div>
                ) : rooms.length === 0 ? (
                    <div className="no-rooms">
                        <p>현재 생성된 방이 없습니다.</p>
                        <button onClick={handleCreateRoom} className="create-room-button">
                            첫 번째 방 만들기
                        </button>
                    </div>
                ) : (
                    <div className="rooms-grid">
                        {rooms.map((room) => (
                            <div
                                key={room.id}
                                className="room-card"
                                onClick={() => handleRoomClick(room.id)}
                            >
                                <h3 className="room-title">{room.title}</h3>
                                <div className="room-info">
                                    <p className="room-host">방장: {room.hostNickName}</p>
                                    <p className="room-participants">
                                        {room.currentParticipants} / {room.maxParticipants}
                                    </p>
                                </div>
                                <p className="room-date">
                                    생성일:{" "}
                                    {new Date(room.createdDate).toLocaleDateString("ko-KR")}
                                </p>
                            </div>
                        ))}
                    </div>
                )}
            </main>
        </div>
    );
}

export default GameRoomPage;