import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";
<<<<<<< Updated upstream

import PageLayout from "./components/PageLayout/PageLayout.jsx";

=======
>>>>>>> Stashed changes
import MainPage from "./pages/MainPage.jsx";
import ChooseGamePage from "./pages/ChooseGamePage.jsx";
import GameRoomPage from "./pages/GameRoomPage.jsx";
import CreateRoomPage from "./pages/CreateRoomPage.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import PasswordReset from "./pages/PasswordReset.jsx";
import ChatRoom from "./pages/ChatRoom.jsx";
import NotificationListener from "./components/NotificationListener.jsx";
import ProfilePage from "./pages/ProfilePage.jsx";
<<<<<<< Updated upstream
import ProfileEditPage from "./pages/ProfileEditPage.jsx";
import ProfilePasswordPage from "./pages/ProfilePasswordPage.jsx";
=======
>>>>>>> Stashed changes

function App() {
  return (
    <Router>
<<<<<<< Updated upstream
      <NotificationListener />

      <Routes>
        {/* ✅ Header가 필요한 모든 페이지 */}
        <Route element={<PageLayout />}>
          <Route path="/" element={<MainPage />} />
          <Route path="/choosegame" element={<ChooseGamePage />} />
          <Route path="/game/:gameId" element={<GameRoomPage />} />
          <Route path="/createroom" element={<CreateRoomPage />} />
          <Route path="/chat/:matchId" element={<ChatRoom />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/profile/edit" element={<ProfileEditPage />} />
          <Route path="/profile/password" element={<ProfilePasswordPage />} />
        </Route>

        {/* ❌ Header가 필요 없는 페이지 */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/password-reset" element={<PasswordReset />} />
      </Routes>
=======
      <div className="app-container">
        {/* 로그인 상태일 때만 동작하도록 내부에서 처리됨 */}
        <NotificationListener />

        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/choosegame" element={<ChooseGamePage />} />
          <Route path="/createroom" element={<CreateRoomPage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/profile" element={<ProfilePage />} />
        </Routes>
      </div>
>>>>>>> Stashed changes
    </Router>
  );
}

export default App;
