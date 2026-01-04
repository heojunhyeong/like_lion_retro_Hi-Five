// src/components/Footer/Footer.jsx
import { useState } from "react";
import "./Footer.css";

/**
 * 팀원 카카오페이 링크 목록
 * name: 표시 이름
 * icon: 이모지 or 나중에 이미지로 교체 가능
 * link: 카카오페이 송금 링크
 */
const TEAM_MEMBERS = [
  { name: "진", icon: "👩‍💻", link: "https://link.kakaopay.com/__/uyl6_9z" },
  { name: "준형", icon: "🧑‍💻", link: "https://link.kakaopay.com/__/eDKHSqB" },
  { name: "윤혁", icon: "👨‍💻", link: "https://link.kakaopay.com/__/ifzQbxv" },
  { name: "지번", icon: "👩‍💻", link: "https://link.kakaopay.com/__/MAQhs5l" },
  { name: "찬혁", icon: "🧑‍💻", link: "https://link.kakaopay.com/__/Y-r6pdu" },
];

const Footer = () => {
  const [showDonate, setShowDonate] = useState(false);
  const [toastMessage, setToastMessage] = useState("");

  const openLink = (member) => {
    window.open(member.link, "_blank");
    setShowDonate(false);

    setToastMessage(`☕ ${member.name} 개발자에게 커피가 전달됩니다!`);
    setTimeout(() => setToastMessage(""), 2500);
  };

  const openRandomLink = () => {
    const randomIndex = Math.floor(Math.random() * TEAM_MEMBERS.length);
    const selected = TEAM_MEMBERS[randomIndex];
    openLink(selected);
  };

  return (
    <>
      <footer className="footer">
        <p className="footer-text">
          © {new Date().getFullYear()} Playmate Team. All rights reserved.
        </p>

        <button className="coffee-button" onClick={() => setShowDonate(true)}>
          ☕ 개발자에게 커피 사주기
        </button>
      </footer>

      {/* ☕ 후원 선택 모달 */}
      {showDonate && (
        <div className="donate-overlay" onClick={() => setShowDonate(false)}>
          <div className="donate-modal" onClick={(e) => e.stopPropagation()}>
            <h3>누구에게 커피를 보낼까요?</h3>
            <button className="random-button" onClick={openRandomLink}>
              <span className="icon">🎲</span>
              <span className="name">랜덤</span>
            </button>
            <div className="member-buttons">
              {TEAM_MEMBERS.map((member) => (
                <button key={member.name} onClick={() => openLink(member)}>
                  <span className="icon">{member.icon}</span>
                  <span className="name">{member.name}</span>
                </button>
              ))}
            </div>
          </div>
        </div>
      )}

      {/* ✅ 토스트 */}
      {toastMessage && <div className="donate-toast">{toastMessage}</div>}
    </>
  );
};

export default Footer;
