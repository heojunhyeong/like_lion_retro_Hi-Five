import { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { connectToChat, sendMessage, disconnect } from '../services/chatService';
import { getToken } from '../api/userApi';
import { getUserIdFromToken } from '../utils/jwtUtils';
import { getMatchDetail, getParticipants, approveParticipant, rejectParticipant } from '../api/matchApi';
import './ChatRoom.css';

function ChatRoom() {
    const { matchId } = useParams();
    const navigate = useNavigate();
    const [messages, setMessages] = useState([]);
    const [inputMessage, setInputMessage] = useState('');
    const [senderId, setSenderId] = useState('');
    const [matchDetail, setMatchDetail] = useState(null);
    const [participants, setParticipants] = useState([]);
    const [isHost, setIsHost] = useState(false);
    const [entryMethod, setEntryMethod] = useState(null);
    const messagesEndRef = useRef(null);

    // 방 정보 및 참가자 목록 로드
    useEffect(() => {
        const loadMatchData = async () => {
            try {
                const token = getToken();
                if (!token) {
                    alert('로그인이 필요합니다.');
                    navigate('/login');
                    return;
                }

                const userId = getUserIdFromToken();
                if (!userId) {
                    console.error('사용자 ID를 찾을 수 없습니다.');
                    alert('사용자 정보를 불러올 수 없습니다.');
                    navigate('/login');
                    return;
                }
                setSenderId(userId);

                // 방 상세 정보 조회
                const matchDetailResponse = await getMatchDetail(matchId);
                const matchData = matchDetailResponse.data;
                setMatchDetail(matchData);
                setEntryMethod(matchData.entryMethod);
                
                // 방장인지 확인
                setIsHost(matchData.hostUserId && userId === matchData.hostUserId);

                // 참가자 목록 조회
                const participantsResponse = await getParticipants(matchId);
                setParticipants(participantsResponse.data || []);

                // WebSocket 연결
                connectToChat(matchId, (message) => {
                    // 서버에서 받은 메시지 처리
                    setMessages((prevMessages) => {
                        const now = new Date().getTime();
                        const messageTimestamp = new Date().toISOString();
                        
                        // 임시 메시지(tempId가 있는)를 찾아서 서버 메시지로 교체
                        const tempMessageIndex = prevMessages.findIndex(prevMsg => 
                            prevMsg.tempId &&
                            prevMsg.content === message.content &&
                            prevMsg.senderId === message.senderId &&
                            prevMsg.timestamp &&
                            (now - new Date(prevMsg.timestamp).getTime()) < 3000
                        );
                        
                        if (tempMessageIndex !== -1) {
                            // 임시 메시지를 서버 메시지로 교체 (tempId 제거)
                            const newMessages = [...prevMessages];
                            newMessages[tempMessageIndex] = { ...message, timestamp: messageTimestamp };
                            return newMessages;
                        }
                        
                        // 중복 메시지 체크: 같은 내용, 같은 발신자, 2초 이내의 메시지는 무시
                        const isDuplicate = prevMessages.some(prevMsg => 
                            prevMsg.content === message.content &&
                            prevMsg.senderId === message.senderId &&
                            prevMsg.timestamp &&
                            (now - new Date(prevMsg.timestamp).getTime()) < 2000
                        );
                        
                        if (isDuplicate) {
                            return prevMessages;
                        }
                        
                        // 새 메시지 추가
                        return [...prevMessages, { ...message, timestamp: messageTimestamp }];
                    });
                });

            } catch (error) {
                console.error('방 정보 로드 실패:', error);
                alert('방 정보를 불러올 수 없습니다.');
            }
        };

        loadMatchData();

        // 컴포넌트 언마운트 시 연결 종료
        return () => {
            disconnect();
        };
    }, [matchId, navigate]);

    // 참가자 목록 새로고침
    const refreshParticipants = async () => {
        try {
            const participantsResponse = await getParticipants(matchId);
            setParticipants(participantsResponse.data || []);
        } catch (error) {
            console.error('참가자 목록 새로고침 실패:', error);
        }
    };

    // 참가자 승인
    const handleApprove = async (participantId) => {
        try {
            await approveParticipant(participantId);
            alert('참가자를 승인했습니다.');
            refreshParticipants();
        } catch (error) {
            console.error('참가자 승인 실패:', error);
            alert(error.message || '참가자 승인에 실패했습니다.');
        }
    };

    // 참가자 거절
    const handleReject = async (participantId) => {
        try {
            await rejectParticipant(participantId);
            alert('참가자를 거절했습니다.');
            refreshParticipants();
        } catch (error) {
            console.error('참가자 거절 실패:', error);
            alert(error.message || '참가자 거절에 실패했습니다.');
        }
    };

    // 메시지가 추가될 때마다 스크롤을 맨 아래로
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    const handleSendMessage = (e) => {
        e.preventDefault();
        if (inputMessage.trim() && senderId) {
            const messageContent = inputMessage.trim();
            const timestamp = new Date().toISOString();
            const tempId = `temp-${Date.now()}-${Math.random()}`;
            
            const messageToSend = {
                matchId: parseInt(matchId),
                senderId: senderId,
                content: messageContent,
                timestamp: timestamp,
                tempId: tempId // 임시 ID로 중복 방지
            };
            
            // 즉시 화면에 메시지 표시 (낙관적 업데이트)
            setMessages((prevMessages) => [...prevMessages, messageToSend]);
            
            // 서버로 메시지 전송
            sendMessage(parseInt(matchId), senderId, messageContent);
            setInputMessage('');
        }
    };

    // 대기 중인 참가자 필터링
    const waitingParticipants = participants.filter(p => p.status === 'WAITING');
    // 승인된 참가자 필터링
    const acceptedParticipants = participants.filter(p => p.status === 'ACCEPTED');

    // 방장인지 확인하는 함수
    const isHostUser = (participantUserId) => {
        return matchDetail?.hostUserId === participantUserId;
    };

    return (
        <div className="chat-room-container">
            <div className="chat-header">
                <button 
                    className="back-button"
                    onClick={() => navigate('/choosegame')}
                >
                    ← 돌아가기
                </button>
                <h2>{matchDetail?.title || `채팅방 (방 ID: ${matchId})`}</h2>
            </div>

            <div className="chat-main-content">
                {/* 참가자 목록 패널 (항상 표시) */}
                <div className="participants-panel">
                    {/* 승인된 참가자 목록 */}
                    <div className="participants-section">
                        <h3>참가자 목록</h3>
                        {acceptedParticipants.length === 0 ? (
                            <div className="no-participants">참가자가 없습니다.</div>
                        ) : (
                            acceptedParticipants.map((participant) => (
                                <div key={participant.participantId} className="participant-item">
                                    <div className="participant-info">
                                        <div className="participant-name">
                                            {participant.nickName}
                                            {isHostUser(participant.userId) && (
                                                <span className="host-badge"> -방장</span>
                                            )}
                                        </div>
                                        <div className="participant-details">
                                            {participant.gender && (
                                                <span className="participant-detail">
                                                    {participant.gender === 'MALE' ? '남성' : participant.gender === 'FEMALE' ? '여성' : participant.gender}
                                                </span>
                                            )}
                                            {participant.age && (
                                                <span className="participant-detail">
                                                    {participant.age === 'TEENS' ? '10대' :
                                                     participant.age === 'TWENTIES' ? '20대' :
                                                     participant.age === 'THIRTIES' ? '30대' :
                                                     participant.age === 'FORTIES' ? '40대' :
                                                     participant.age === 'FIFTIES_PLUS' ? '50대 이상' : participant.age}
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>

                    {/* 대기 중인 참가자 목록 (방장 승인 방식이고 방장일 때만 표시) */}
                    {entryMethod === 'APPROVAL' && isHost && waitingParticipants.length > 0 && (
                        <div className="waiting-participants-section">
                            <h3 className="waiting-title">대기 중인 참가자</h3>
                            {waitingParticipants.map((participant) => (
                                <div key={participant.participantId} className="participant-item waiting-item">
                                    <div className="participant-info">
                                        <div className="participant-name">{participant.nickName}</div>
                                        <div className="participant-details">
                                            {participant.gender && (
                                                <span className="participant-detail">
                                                    {participant.gender === 'MALE' ? '남성' : participant.gender === 'FEMALE' ? '여성' : participant.gender}
                                                </span>
                                            )}
                                            {participant.age && (
                                                <span className="participant-detail">
                                                    {participant.age === 'TEENS' ? '10대' :
                                                     participant.age === 'TWENTIES' ? '20대' :
                                                     participant.age === 'THIRTIES' ? '30대' :
                                                     participant.age === 'FORTIES' ? '40대' :
                                                     participant.age === 'FIFTIES_PLUS' ? '50대 이상' : participant.age}
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="participant-actions">
                                        <button 
                                            className="approve-button"
                                            onClick={() => handleApprove(participant.participantId)}
                                        >
                                            수락
                                        </button>
                                        <button 
                                            className="reject-button"
                                            onClick={() => handleReject(participant.participantId)}
                                        >
                                            거절
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {/* 채팅 메시지 영역 */}
                <div className="chat-area">
                    <div className="messages-container">
                        {messages.length === 0 ? (
                            <div className="no-messages">
                                아직 메시지가 없습니다. 첫 메시지를 입력해보세요!
                            </div>
                        ) : (
                            messages.map((message, index) => (
                                <div 
                                    key={message.tempId || `msg-${message.timestamp}-${index}`} 
                                    className={`message ${message.senderId === senderId ? 'own-message' : 'other-message'}`}
                                >
                                    <div className="message-sender">{message.senderId}</div>
                                    <div className="message-content">{message.content}</div>
                                </div>
                            ))
                        )}
                        <div ref={messagesEndRef} />
                    </div>

                    <form className="message-input-form" onSubmit={handleSendMessage}>
                        <input
                            type="text"
                            value={inputMessage}
                            onChange={(e) => setInputMessage(e.target.value)}
                            placeholder="메시지를 입력하세요..."
                            className="message-input"
                        />
                        <button type="submit" className="send-button">
                            전송
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default ChatRoom;
