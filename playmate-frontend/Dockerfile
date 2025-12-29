FROM node:22-alpine AS builder
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

# 도커 빌드 시 환경 변수 설정 (nginx 프록시 사용)
# 빈 문자열을 사용하면 상대 경로로 API 호출 (/api → nginx → 백엔드)
ARG VITE_API_URL=""
ENV VITE_API_URL=$VITE_API_URL

#EXPOSE 3000
RUN npm run build

#Nginx
FROM nginx:1.28.1-alpine
COPY --from=builder /app/dist /usr/share/nginx/html/

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]