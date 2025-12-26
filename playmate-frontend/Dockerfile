FROM 22-alpine AS builder
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
EXPOSE 3000
RUN npm run build #실제 배포할 때 사용

#Nginx
FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html/

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]