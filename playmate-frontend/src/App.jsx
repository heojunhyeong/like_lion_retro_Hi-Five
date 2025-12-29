/**
 * 작업내용: 매칭방 알림 기능 통합 - 전역 알림 컨테이너 추가 및 사용자 ID 관리
 * 
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useState, useEffect } from 'react';
import './App.css'
import MainPage from "./pages/MainPage.jsx";
import ChooseGamePage from "./pages/ChooseGamePage.jsx";
import CreateRoomPage from "./pages/CreateRoomPage.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import NotificationContainer from "./components/NotificationContainer.jsx";
import { getUserId } from "./api/userApi.js";

function App() {
    const [userId, setUserId] = useState(null);

    useEffect(() => {
        // 로그인된 사용자 ID 확인
        const currentUserId = getUserId();
        setUserId(currentUserId);

        // localStorage 변경 감지 (다른 탭에서 로그인/로그아웃 시)
        const handleStorageChange = () => {
            const newUserId = getUserId();
            setUserId(newUserId);
        };

        window.addEventListener('storage', handleStorageChange);
        
        // 현재 탭에서의 localStorage 변경도 감지하기 위한 커스텀 이벤트
        window.addEventListener('userIdChanged', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
            window.removeEventListener('userIdChanged', handleStorageChange);
        };
    }, []);

    return (
        <Router>
            <div className="app-container">
                <Routes>
                    <Route path="/" element={<MainPage />} />
                    <Route path="/choosegame" element={<ChooseGamePage />} />
                    <Route path="/createroom" element={<CreateRoomPage />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                </Routes>
                {/* 전역 알림 컨테이너 - 로그인된 사용자에게만 표시 */}
                {userId && <NotificationContainer userId={userId} />}
            </div>
        </Router>
    );
}

export default App;