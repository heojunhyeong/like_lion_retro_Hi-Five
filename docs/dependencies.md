# Backend Dependencies Guide

## 기본 원칙

- 의존성 추가 시 팀 공유 필수
- 목적 없는 의존성 추가 금지
- 가능한 spring-boot-starter 사용

## 현재 사용 중인 의존성

### spring-boot-starter-web

- 목적: REST API 서버
- 사용 위치: Controller, ExceptionHandler

### spring-boot-starter-data-jpa

- 목적: ORM 기반 DB 접근
- 사용 위치: Repository, Entity

### mysql-connector-j

- 목적: MySQL 연결
- 사용 위치: runtime only

## 추가 시 기록 형식

- 의존성 이름:
- 추가 이유:
- 사용 클래스/패키지:
- 추가자:
