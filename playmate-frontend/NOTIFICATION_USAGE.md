# 매칭방 알림 기능 사용 가이드

이 문서는 매칭방 입장 요청/입장/승인 알림 기능의 프론트엔드 사용 방법을 설명합니다.

## 개요

매칭방 알림 시스템은 SSE(Server-Sent Events)를 사용하여 실시간 알림을 전송합니다.

### 주요 기능
- **입장 요청 알림**: 유저가 방에 입장 요청 시 방장에게 알림 전송
- **입장 완료 알림**: 유저가 즉시 입장 시 방장에게 알림 전송
- **입장 허가 알림**: 방장이 유저의 입장을 허가 시 유저에게 알림 전송

## 구조

```
src/
├── api/
│   └── roomApi.js                    # 매칭방 알림 API 함수들
├── services/
│   └── sseService.js                 # SSE 연결 관리 서비스
├── components/
│   ├── NotificationToast.jsx         # 알림 토스트 UI 컴포넌트
│   ├── NotificationToast.css         # 알림 토스트 스타일
│   └── NotificationContainer.jsx     # 알림 컨테이너 컴포넌트
├── hooks/
│   └── useNotifications.js           # 알림 관리를 위한 커스텀 훅
└── utils/
    └── roomNotificationHelper.js     # 알림 전송 헬퍼 함수
```

## 자동 설정

`App.jsx`에 `NotificationContainer`가 이미 통합되어 있습니다. 로그인된 사용자는 자동으로 SSE 연결이 생성되고 알림을 받을 수 있습니다.

## 사용 방법

### 1. 알림 전송 (매칭방 페이지에서 사용)

매칭방 관련 페이지에서 다음 헬퍼 함수들을 사용하여 알림을 전송할 수 있습니다.

#### 입장 요청 알림 전송

```javascript
import { sendJoinRequestNotification } from "../utils/roomNotificationHelper";

const handleRequestJoin = async () => {
    try {
        const roomId = "room123";
        const hostId = "host456";
        const userName = "사용자닉네임"; // 현재 사용자의 닉네임
        
        await sendJoinRequestNotification(roomId, hostId, userName);
        console.log("입장 요청 알림이 방장에게 전송되었습니다.");
        
        // 추가 로직 (예: 요청 상태 업데이트 등)
    } catch (error) {
        console.error("알림 전송 실패:", error);
        alert(error.message);
    }
};
```

#### 즉시 입장 알림 전송

```javascript
import { sendJoinNotification } from "../utils/roomNotificationHelper";

const handleJoinRoom = async () => {
    try {
        const roomId = "room123";
        const hostId = "host456";
        const userName = "사용자닉네임";
        
        await sendJoinNotification(roomId, hostId, userName);
        console.log("입장 완료 알림이 방장에게 전송되었습니다.");
        
        // 추가 로직 (예: 방 참여자 목록 업데이트 등)
    } catch (error) {
        console.error("알림 전송 실패:", error);
        alert(error.message);
    }
};
```

#### 입장 허가 알림 전송 (방장이 사용)

```javascript
import { sendApprovalNotification } from "../utils/roomNotificationHelper";

const handleApproveJoin = async (requestingUserId) => {
    try {
        const roomId = "room123";
        
        await sendApprovalNotification(roomId, requestingUserId);
        console.log("입장 허가 알림이 유저에게 전송되었습니다.");
        
        // 추가 로직 (예: 참여자 목록 업데이트 등)
    } catch (error) {
        console.error("알림 전송 실패:", error);
        alert(error.message);
    }
};
```

### 2. 직접 API 호출 (고급 사용자)

헬퍼 함수 대신 직접 API를 호출할 수도 있습니다.

```javascript
import { requestJoinRoom, joinRoom, approveJoinRoom } from "../api/roomApi";
import { getUserId } from "../api/userApi";

// 입장 요청 알림
const userId = getUserId();
await requestJoinRoom(roomId, userId, userName, hostId);

// 입장 완료 알림
await joinRoom(roomId, userId, userName, hostId);

// 입장 허가 알림
await approveJoinRoom(roomId, hostId, userId);
```

### 3. 커스텀 알림 처리 (useNotifications 훅 사용)

특정 페이지에서 알림을 직접 처리하고 싶은 경우:

```javascript
import { useNotifications } from "../hooks/useNotifications";
import { getUserId } from "../api/userApi";

function MyComponent() {
    const userId = getUserId();
    const { notifications, removeNotification, isConnected } = useNotifications(userId);

    return (
        <div>
            <div>SSE 연결 상태: {isConnected ? "연결됨" : "연결 안 됨"}</div>
            <div>알림 개수: {notifications.length}</div>
            
            {notifications.map((notif) => (
                <div key={notif.id}>
                    <p>{notif.message}</p>
                    <button onClick={() => removeNotification(notif.id)}>
                        닫기
                    </button>
                </div>
            ))}
        </div>
    );
}
```

## 알림 타입

알림 객체는 다음과 같은 구조를 가집니다:

```typescript
{
    type: "ROOM_JOIN_REQUEST" | "ROOM_JOINED" | "ROOM_JOIN_APPROVED",
    message: string,      // 알림 메시지
    roomId: string,       // 방 ID
    userId: string        // 관련 유저 ID
}
```

### 알림 타입별 설명

- **ROOM_JOIN_REQUEST**: 유저가 방에 입장 요청을 했을 때 방장이 받는 알림
- **ROOM_JOINED**: 유저가 방에 즉시 입장했을 때 방장이 받는 알림
- **ROOM_JOIN_APPROVED**: 방장이 유저의 입장을 허가했을 때 유저가 받는 알림

## 주의사항

1. **로그인 필요**: 모든 알림 기능은 로그인된 사용자만 사용할 수 있습니다.
2. **사용자 ID 저장**: 로그인 시 사용자 ID가 localStorage에 저장됩니다.
3. **SSE 연결**: 로그인 시 자동으로 SSE 연결이 생성되며, 로그아웃 시 연결이 종료됩니다.
4. **CORS 설정**: 백엔드의 CORS 설정이 올바르게 되어 있는지 확인하세요. (현재 `/api/**`만 허용되어 있어 `/room`과 `/sse` 경로에 대한 CORS 설정이 필요할 수 있습니다.)

## 백엔드 API 엔드포인트

- `POST /room/{roomId}/request` - 입장 요청 알림 전송
- `POST /room/{roomId}/join` - 입장 완료 알림 전송
- `POST /room/{roomId}/approve` - 입장 허가 알림 전송
- `GET /sse/connect?clientId={userId}` - SSE 연결

## 문제 해결

### 알림이 표시되지 않는 경우

1. 사용자가 로그인되어 있는지 확인
2. 브라우저 콘솔에서 SSE 연결 오류 확인
3. 백엔드 서버가 정상적으로 실행 중인지 확인
4. 네트워크 탭에서 SSE 연결 상태 확인

### CORS 오류가 발생하는 경우

백엔드의 `SecurityConfig.java`에서 `/room/**`와 `/sse/**` 경로에 대한 CORS 설정을 추가해야 합니다.

