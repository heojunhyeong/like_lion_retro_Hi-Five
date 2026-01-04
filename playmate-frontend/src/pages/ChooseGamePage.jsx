import { useNavigate } from "react-router-dom";
import "./ChooseGamePage.css";

function ChooseGamePage() {
  const navigate = useNavigate();

  const handleGameClick = (gameId) => {
    navigate(`/game/${gameId}`);
  };

  const games = [
    {
      name: "FC온라인",
      id: "fc_online",
      category: "FC_ONLINE",
      image: "/images/Fconline.jpg",
    },
    {
      name: "리그오브레전드",
      id: "league_of_legends",
      category: "LEAGUE_OF_LEGENDS",
      image: "/images/Lol.jpg",
    },
    {
      name: "오버워치",
      id: "overwatch",
      category: "OVER_WATCH",
      image: "/images/Overwatch.jpg",
    },
    {
      name: "메이플스토리",
      id: "maple_story",
      category: "MAPLE_STORY",
      image: "/images/Maple.jpg",
    },
    {
      name: "기타",
      id: "hobby",
      category: "OTHER",
      image: "/images/hobby.jpg",
    },
  ];

  return (
    <div className="choose-game-page">
      {/* 메인 컨텐츠 */}
      <main className="main-content">
        <div className="games-section">
          <div className="games-grid">
            {games.map((game) => (
              <div
                key={game.id}
                className="game-card"
                onClick={() => handleGameClick(game.id)}
                style={{ cursor: "pointer" }}
              >
                <div className="game-card-content">
                  <div className="game-image-container">
                    <img
                      src={game.image}
                      alt={game.name}
                      className="game-image"
                      onError={(e) => {
                        e.target.style.display = "none";
                      }}
                    />
                  </div>
                  <h3 className="game-name">{game.name}</h3>
                </div>
              </div>
            ))}
          </div>
        </div>
      </main>
    </div>
  );
}

export default ChooseGamePage;
