import { useNavigate } from 'react-router-dom';
import './MainPage.css';

function MainPage() {
    const navigate = useNavigate();

    const handleLoginClick = () => {
        navigate('/login');
    };

    const handleRegisterClick = () => {
        navigate('/register');
    };

    return (
        <div className="main-page">
            <div className="main-container">
                <h1>메인 페이지</h1>
                <div className="button-container">
                    <button onClick={handleLoginClick} className="login-button">
                        로그인
                    </button>
                    <button onClick={handleRegisterClick} className="register-button">
                        회원가입
                    </button>
                </div>
            </div>
        </div>
    );
}

export default MainPage;

