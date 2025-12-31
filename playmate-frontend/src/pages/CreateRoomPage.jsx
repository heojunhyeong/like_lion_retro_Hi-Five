import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getToken, removeToken } from "../api/userApi";
import { getMatches } from "../api/matchApi";
import "./GameRoomPage.css";

// 게임 ID와 카테고리 매핑
const GAME_CATEGORY_MAP = {
    fc_online: "FC_ONLINE", // PreferCategory에 맞게 조정 필요
    league_of_legends: "League_Of_Legends",
    overwatch: "OVER_WATCH",
    maple_story: "MAPLE_STORY", // PreferCategory에 맞게 조정 필요
};

const GAME_NAME_MAP = {
    fc_online: "FC온라인",
    league_of_legends: "리그오브레전드",
    overwatch: "오버워치",
    maple_story: "메이플스토리",
};

function GameRoomPage() {
    const navigate = useNavigate();
    const { gameId } = useParams();
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [rooms, setRooms] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = getToken();
        setIsLoggedIn(!!token);
    }, []);

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
                const roomList = await getMatches(category);
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
    }, [gameId, navigate]);

    const handleLoginClick = () => {
        navigate("/login");
    };

    const handleRegisterClick = () => {
        navigate("/register");
    };

    const handleLogoutClick = () => {
        removeToken();
        setIsLoggedIn(false);
        navigate("/");
    };

    const handleBackClick = () => {
        navigate("/choosegame");
    };

    const handleRoomClick = (roomId) => {
        // TODO: 방 상세 페이지로 이동 또는 방 참여 기능 구현
        console.log("방 클릭:", roomId);
    };

    const handleCreateRoom = () => {
        if (!getToken()) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }
        navigate("/createroom");
    };

    return (
        <div className="game-room-page">
            {/* 헤더 */}
            <header className="main-header">
                <div className="header-content">
                    <div className="header-left">
                        <div className="logo">PlayMate</div>
                    </div>
                    <div className="header-right">
                        {isLoggedIn ? (
                            <button
                                onClick={handleLogoutClick}
                                className="logout-header-button"
                            >
                                로그아웃
                            </button>
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
            {/* 메인 컨텐츠 */}
            <main className="main-content">
                <div className="game-room-controls">
                    <button onClick={handleBackClick} className="back-button">
                        ← 뒤로가기
                    </button>
                    <div className="game-room-header">
                        <h2 className="game-title">
                            {GAME_NAME_MAP[gameId] || "게임"} 방 목록
                        </h2>
                    </div>
                    <button onClick={handleCreateRoom} className="create-room-button">
                        방 만들기
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
