import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css'
import MainPage from "./pages/MainPage.jsx";
import ChooseGamePage from "./pages/ChooseGamePage.jsx";
import CreateRoomPage from "./pages/CreateRoomPage.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import PasswordReset from "./pages/PasswordReset.jsx";
import ChatRoom from "./pages/ChatRoom.jsx";
import NotificationListener from "./components/NotificationListener.jsx";

function App() {
    return (
        <Router>
            <div className="app-container">
                {/* 로그인 상태일 때만 동작하도록 내부에서 처리됨 */}
                <NotificationListener />

                <Routes>
                    <Route path="/" element={<MainPage />} />
                    <Route path="/choosegame" element={<ChooseGamePage />} />
                    <Route path="/createroom" element={<CreateRoomPage />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/password-reset" element={<PasswordReset />} />
                    <Route path="/chat/:matchId" element={<ChatRoom />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;