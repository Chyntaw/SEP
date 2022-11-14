FROM node:alpine AS builder
WORKDIR /app
COPY . .
RUN npm install && npm install bootstrap@5.2.0 && npm run build

FROM nginx:alpine
COPY --from=builder /app/dist/* /usr/share/nginx/html/
COPY /nginx.conf /etc/nginx/conf.d/default.conf
