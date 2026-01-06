# Play Mate

---

## 1. 프로젝트 개요
다양한 게임 혹은 취미생활을 같이 즐길 사람들을 찾는 매칭 커뮤니티

---

## 2. 기술 스택

| 구분 | 기술 스택 |
| :--- | :--- |
| 백엔드 | Spring Boot |
| 프론트엔드 | React |
| 데이터베이스 | MySQL |
| 프록시 | Nginx |
| 컨테이너 | Docker, Docker Compose |
| 배포 | AWS EC2 |
| 실시간 통신 | WebSocket (STOMP), SSE (Server-Sent Events) |
| 보안 | Spring Security, JWT |
---

## API 명세
[02.api-spec.md](02.api-spec.md)

---
## 3. 디렉토리 구조

```
like_lion_retro_Hi-Five
├─ playmate-backend
│   ├─ src
│   ├─ Dockerfile
│   └─ build.gradle
├─ playmate-frontend
│   ├─ public
│   ├─ src
│   └─ Dockerfile
├─ nginx
│   ├─ default.conf
│   └─ Dockerfile
└─  docker-compose.yml
```
---
# 로컬 개발 환경 실행 가이드

이 프로젝트는 Docker를 사용하여 로컬 환경을 빠르고 일관되게 구축할 수 있도록 구성되었습니다. 각 파트별 또는 전체 시스템 실행 방법은 다음과 같습니다.

## 1. 백엔드 개발 환경 실행
백엔드 서버와 데이터베이스를 실행

```bash
# 백엔드 디렉토리로 이동
cd playmate-backend

# 개발용 도커 컴포즈 실행 (빌드 포함)
docker compose -f docker-compose.dev.yml up --build
```

## 2. 프론트엔드 개발 환경 실행
```bash
# 프론트엔드 디렉토리로 이동
cd playmate-frontend

# 개발용 도커 이미지 빌드
docker build -f Dockerfile.dev -t playmate-frontend:dev .

# 컨테이너 실행 (3000번 포트 연결 및 종료 시 컨테이너 자동 제거)
docker run --rm -p 3000:3000 playmate-frontend:dev
```

## 3. 전체 시스템 통합 실행
```bash
# 프로젝트 루트 디렉토리에서 실행
docker compose -f docker-compose.dev.yml up --build
```
### 접근 주소 : http://localhost:70/