# Service-Based System Demo

Demo cơ bản theo kiến trúc service-based gồm:

- order-service (Spring Boot)
- payment-service (Spring Boot)
- shipping-service (Spring Boot)
- api-gateway (Spring Cloud Gateway)
- frontend (React + Vite + Nginx)
- 1 shared database (PostgreSQL)

## 1. Chạy toàn bộ bằng Docker Compose

```bash
# Máy mạnh (lên nhanh hơn)
docker compose -f docker-compose.yml -f docker-compose.fast.yml up --build

# Máy yếu (ổn định hơn)
docker compose -f docker-compose.yml -f docker-compose.safe.yml up --build
```

Sau khi chạy:

- Frontend: http://localhost:5173
- Gateway: http://localhost:8080
- Order API qua Gateway: http://localhost:8080/api/orders
- Payment API qua Gateway: http://localhost:8080/api/payments
- Shipping API qua Gateway: http://localhost:8080/api/shipments

## 2. API health check

- http://localhost:8080/api/gateway/health
- http://localhost:8080/api/orders/health
- http://localhost:8080/api/payments/health
- http://localhost:8080/api/shipments/health

## 3. Ghi chú

- 3 service dùng chung 1 DB: `my_shared_db`.
- Mỗi service có bảng riêng và dữ liệu mẫu trong `data.sql`.
- Frontend gọi vào API Gateway (port 8080), gateway định tuyến đến 3 service.
- File profile healthcheck:
  - `docker-compose.fast.yml`: ưu tiên tốc độ startup.
  - `docker-compose.safe.yml`: ưu tiên độ ổn định trên máy cấu hình thấp.
