FROM node:alpine AS builder
WORKDIR /app
COPY . .
RUN npm install && npm install bootstrap@5.2.0 && npm install ng2-search-filter && npm install chart.js && npm run build

FROM nginx:alpine
COPY --from=builder /app/dist/* /usr/share/nginx/html/
COPY /nginx.conf /etc/nginx/conf.d/default.conf
