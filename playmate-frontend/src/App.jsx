import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css'
import MainPage from "./pages/MainPage.jsx";
import ChooseGamePage from "./pages/ChooseGamePage.jsx";
import CreateRoomPage from "./pages/CreateRoomPage.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";

function App() {
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
            </div>
        </Router>
    );
}

export default App;