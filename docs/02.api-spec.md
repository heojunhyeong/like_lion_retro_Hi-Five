# Playmate API 명세서

---
## 1. 회원가입 및 인증


---

### 1.1 회원가입

- **Endpoint:** `/api/users/register`
- **Method:** `POST`
- **Request Header:** `없음`
- **Request Body:**

```json
{
  "userId" : "playmate1",
  "password" : "Password123!",
  "cofirmPassword" : "Password123!",
  "nickname" : "Playmate",
  "email" : "playmate@example.com",
  "gender" : "MALE",
  "preferCategory" : "LEAGUE_OF_LEGENDS",
  "age" : "TWENTIES"
}
```

- **Response Body:**

```json
{
  "success": true,
  "data": 1,
  "message": null
}
```

- **Status Code:** `200 OK`
- **Description:** 새로운 회원을 등록합니다. `data` 값은 생성된 유저의 PK.

---

### 1.2 로그인

- **Endpoint:** `/api/users/login`
- **Method:** `POST`
- **Request Header:** `200 OK`
- **Request Body:** 


```json
{
  "userID" : "user123",
  "userPassword" : "User@1234"
}
```

- **Response Body:**
```json
{
  "accessToken": "eyJhbG...",
  "refreshToken": "eyJhbG..."
}

```
- **Status Code:** `200 OK`
- **Description:** 로그인 성공시 `Access/Refresh` 토큰을 반환 

---

### 1.3 토큰 재발급

- **Endpoint:** `/api/users/refresh`
- **Method:** `GET`
- **Request Header:** `없음`
- **Request Body:**
```json
{
  "refreshToken": "eyJhbG..."
}
```
- **Response Body:**

```json
{
  "accessToken": "eyJhbG..."
}
```

- **Status Code:** `200 OK` , `401 Unauthorized`
- **Description:** 만료된 `Access Token`을 `Refresh Token` 통해 갱신

---

### 1.4 로그아웃

- **Endpoint:** `/api/users/me`
- **Method:** `GET`
- **Request Header:** `Authorization: Bearer {AccessToken}`
- **Request Body:** `없음`
- **Response Body:** `없음`
- **Status Code:** `200 OK`
- **Description:** 서버의 `Refresh Token`을 제거하여 로그아웃 처리

---

## 2. 프로필 및 보안 관리

---

### 2.1 내 프로필 조회

- **Endpoint:** `/api/users/me`
- **Method:** `GET`
- **Request Header:** `Authorization: Bearer {AccessToken}`
- **Request Body:** `없음`
- **Response Body:**

```json
{
  "userId": "playmate1",
  "userEmail": "mate@example.com",
  "nickName": "메이트",
  "preferCategory": "LEAGUE_OF_LEGENDS",
  "gender": "MALE",
  "age": "TWENTIES",
  "introduction": "반갑습니다!"
}
```

- **Status Code:** `200 OK`
- **Description:** 현재 로그인한 사용자의 프로필 정보를 조회

---

### 2.2 내 프로필 수정

- **Endpoint:** `/api/users/me`
- **Method:** `PUT`
- **Request Header:** `Authorization: Bearer {AccessToken}`
- **Request Body:**

```json
{
  "nickName": "새닉네임",
  "preferCategory": "OVER_WATCH",
  "introduction": "소개글 수정합니다."
}
```

- **Response Body:** `없음`
- **Status Code:** `200 OK`
- **Description:** 닉네임, 선호 카테고리, 자기소개 업데이트

---

### 2.3  현재 비밀번호 검증

- **Endpoint:** `/api/users/me/password/verify`
- **Method:** `POST`
- **Request Header:** `Authorization: Bearer {AccessToken}`
- **Request Body:**

```json
{
  "password": "currentPassword"
}
```

- **Response Body:** `없음`
- **Status Code:** `200 OK`
- **Description:** 비밀번호 변경 전, 현재 비밀번호가 맞는지 확인

---

### 2.4 비밀변호 재성정 (로그인 한 뒤)

- **Endpoint:** `/api/users/me/password`
- **Method:** `PUT`
- **Request Header:** `Authorization: Bearer {AccessToken}`
- **Request Body:** 

```json
{
  "currentPassword": "currentPassword",
  "newPassword": "Newpassword123!"
}

```

- **Response Body:** `없음`

- **Status Code:** `200 OK`
- **Description:** 기존 비밀번호 확인 후 새 비밀번호로 변경

---

### 2.5 비밀번호 재설정 메일 요청 (로그아웃 상태)

- **Endpoint:** `/password/reset/request`
- **Method:** `POST`
- **Request Header:** `없음`
- **Request Body:** 
```json
{
  "email": "user@example.com"
}
```
- **Response Body:** `없음`
- **Status Code:** `200 OK`
- **Description:** 비밀번호 재설정 토큰이 포함된 링크를 이메일로 발송
---

### 2.6 비밀번호 재설정 실행 

- **Endpoint:** `/password/reset`
- **Method:** `POST`
- **Request Header:** `없음`
- **Request Body:**
```json
{
  "token": "uuid-token-string",
  "newPassword": "Newpassword123!"
}
```
- **Response Body:** `없음`
- **Status Code:** 200 OK
- **Description:** 메일 토큰 검증 후 비밀번호 초기화

---
## 3. 매칭 시스템

---

### 3.1 매칭방 목록 조회(검색)

- **Endpoint:** `/api/matches`
- **Method:** `GET`
- **Request Header:** `없음`
- **Request Body:** `없음`
- **Response Body:**

```json
{
    "id": 1,
    "title": "롤 같이 해요",
    "hostNickName": "여자만",
    "currentParticipants": 1,
    "maxParticipants": 5
  }
```

- **Status Code:** 200 OK
- **Description:** 필터 조건에 맞는 매칭방 목록을 반환

---

### 3.2 매칭방 생성

- **Endpoint:** `/api/matches`
- **Method:** `POST`
- **Request Header:** `Authorization: Bearer {AccessToken}`
- **Request Body:** 

```json
{
  "title" : "칼바람 같이 하실분 여자만",
  "category" : "LEAGUE_OF_LEGENDS",
  "maxParticipants" : "5", 
  "entryMethod" : "APPROVAL"
}
```

- **Response Body:**

```json
{
  "success": true,
  "data": 10,
  "message": null
}
``` 

- **Status Code:** 200 OK
- **Description:** 방장이 설정한 조건으로 방 생성(`성별`, `나이`, `실력`은 선택사항)

---

###  3.3 매칭방 퇴장

- **Endpoint:** `/api/matches/{matchId}/leave`
- **Method:** `POST`
- **Request Header:** `Authorization: Bearer {AccessToken}'
- **Request Body:** `없음`
- **Response Body:**

```json
{
  "success": true,
  "data": null,
  "message": null
}
```

- **Status Code:** 200 OK
- **Description:** 참여 중인 방에서 퇴장. 퇴장시 방 삭제
---
### 3.4 매칭방 상세 정보 조회

- **Endpoint:** `/api/matches/{matchId}`
- **Method:** `GET`
- **Request Header:** `없음` 
- **Request Body:** `없음`
- **Response Body:**

```json
{
  "success": true,
  "data": {
    "id": 10,
    "title": "롤 실버 이상 구함",
    "content": "매너 게임 하실 분만 오세요.",
    "category": "LEAGUE_OF_LEGENDS",
    "hostNickname": "최고수",
    "maxParticipants": 5,
    "currentParticipants": 2,
    "entryMethod": "APPROVAL"
  },
  "message": null
}
```
- **Status Code:** `200 OK` 
- **Description:** 특정 매칭방의 모든 세부 정보를 조회
---
### 3.5 매칭방 참가자 목록 조회
- **Endpoint:** `/api/matches/{matchId}/participants`
- **Method:** `GET`
- **Request Header:** `없음`
- **Request Body:** `없음`
- **Response Body:**

```json
{
  "success": true,
  "data": [
    { "participantId": 1, "nickname": "유저1", "status": "APPROVED" },
    { "participantId": 2, "nickname": "유저2", "status": "PENDING" }
  ],
  "message": null
}
```

- **Status Code:** `200 OK`
- **Description:** 해당 방에 참여중이거나 대기 중인 모든 유저의 목록 조회
---
### 3.6 참가자 승인 (방장 전용)
- **Endpoint:** `api/matches/participants/{participantId}/approve`
- **Method:** `POST`
- **Request Header:** `Authorization: Bearer {AccessToken}`
- **Request Body:** `없음`
- **Response Body:**

```json
{
  "success": true,
  "data": null,
  "message": null
}
```

- **Status Code:** `200 OK`
- **Description:** 방장이 대기 중인 참가자의 입장을 수락, 성공 시 해당 유저는 채팅 및 매칭 멤버로 확정
---
### 3.7 참가자 거절/추방 (방장 전용)
- **Endpoint:** `/api/matches/participants/{participantId}/reject`
- **Method:** `POST`
- **Request Header:**`Authorization: Bearer {AccessToken}`
- **Request Body:** `없음`
- **Response Body:**

```json
{
  "success": true,
  "data": null,
  "message": null
}
```

- **Status Code:** `200 OK`
- **Description:** 방장이 대기 중인 참가자의 요청을 거절하거나, 기존 참가자를 거절
---
## 실시간 알림 및 채팅

---

### 4.1 SSE 알림 구독

- **Endpoint:** `/api/notifications/subscribe`
- **Method:** `GET`
- **Request Header:** `Authorization: Bearer {AccessToken}`
- **Request Body:** `없음`
- **Response Body:** `text/event-stream`
- **Status Code:** 200 OK
- **Description:** 서버의 실시간 알림을 받기 위한 스트림 연결

---

### 4.2 채팅 메시지 전송(발행) (STOMP)
.
- **Endpoint:** `/pub/chat/message`
- **Method:** `SEND (Websocket)`
- **Request Header:** `Authorization: Bearer {AccessToken}`
- **Request Body:**
```json
{
  "matchId": 1,
  "content": "안녕하세요!"
}
```
- **Response Body:** `없음`
- **Status Code:** `없음(메시지 브로커 처리)`
- **Description:** 특정 매칭방에 채팅 메시지를 발행

---

### 4.3 채팅 메시지 수신(구독) (STOMP)

- **Endpoint:** `/sub/chat/room/{matchId}`
- **Method:** `SUBSCRIBE (Websocket)`
- **Request Header:** `없음`
- **Request Body:** `없음`
- **Response Body:** 
```json
{
  "matchId" : "1",
  "senderId" : "test",
  "content" : "어디사는 몇살?"
}
```
- **Status Code:** `없음`
- **Description:** 해당 방을 구독 중인 유저들에게 메시지가 브로드캐스팅됨











