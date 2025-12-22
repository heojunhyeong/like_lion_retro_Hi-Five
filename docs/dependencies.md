# Backend Dependencies Guide

## 기본 원칙

- 의존성 추가 시 팀 공유 필수
- 목적 없는 의존성 추가 금지
- 가능한 spring-boot-starter 사용
- 개발용 / 테스트용 / 운영용 의존성 구분 명확히

## 현재 사용 중인 의존성

### spring-boot-starter-web

- 목적: REST API 서버 구성
- 사용 위치: Controller, ExceptionHandler
- 비고: 내장 Tomcat 포함

---

### spring-boot-starter-data-jpa

- 목적: ORM 기반 DB 접근
- 사용 위치: Entity, Repository, Service
- 비고: Hibernate 기반 JPA 구현체 사용

---

### mysql-connector-j

- 목적: MySQL 데이터베이스 연결
- 사용 위치: runtime only
- 비고: 운영 환경 DB 드라이버

---

### lombok

- 목적: 보일러플레이트 코드 제거 (getter, setter, constructor 등)
- 사용 위치: Entity, DTO
- 비고: compileOnly / annotationProcessor 사용

---

### spring-boot-devtools

- 목적: 개발 생산성 향상 (자동 재시작, 캐시 비활성화 등)
- 사용 위치: 개발 환경 전용
- 비고: 운영 환경에는 포함되지 않음

---

### spring-boot-starter-test

- 목적: 단위 테스트 및 통합 테스트 지원
- 사용 위치: test 패키지
- 포함 모듈: JUnit, Mockito, Spring Test
- 비고: 테스트 전용 의존성

---

### junit-platform-launcher

- 목적: JUnit 테스트 실행 지원
- 사용 위치: test runtime only
- 비고: 테스트 실행 시 필요

## 추가 시 기록 형식

- 의존성 이름:
- 추가 이유:
- 사용 클래스/패키지:
- 추가자:
