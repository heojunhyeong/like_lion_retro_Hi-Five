
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getToken, removeToken } from "../api/userApi";
import "./MainPage.css";

function MainPage() {
    const navigate = useNavigate();

    const handleCreateRoom = () => {
        if (!getToken()) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }
        navigate("/createroom");
    };

    const handleJoinRoom = () => {
        navigate("/choosegame");
    };

    return (
        <div className="main-page">
            {/* 메인 컨텐츠 */}
            <main className="main-content">
                {/* 사이트 소개 섹션 */}
                <div className="intro-section">
                    <div className="intro-box">
                        <h2 className="intro-title">Match the Play.
                            Find Your Playmate</h2>
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