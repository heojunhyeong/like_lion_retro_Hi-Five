import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";

import PageLayout from "./components/PageLayout/PageLayout.jsx";


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

import ProfileEditPage from "./pages/ProfileEditPage.jsx";
import ProfilePasswordPage from "./pages/ProfilePasswordPage.jsx";

function App() {
  return (
    <Router>

      <NotificationListener />

      <Routes>
        {/* ✅ Header,footer가 필요한 모든 페이지 */}
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

        {/* ❌ Header,footer가 필요 없는 페이지 */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/password-reset" element={<PasswordReset />} />
      </Routes>

    </Router>
  );
}

export default App;
