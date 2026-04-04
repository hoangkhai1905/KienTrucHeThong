# STAGE 1: Cài đặt Dependencies (Giai đoạn chuẩn bị)
FROM python:3.9-alpine AS builder
WORKDIR /app

# Cài đặt các công cụ cần thiết để build (nếu thư viện yêu cầu compile)
RUN apk add --no-cache gcc musl-dev linux-headers

COPY requirements.txt .
# Cài đặt thư viện vào một thư mục riêng để dễ copy
RUN pip install --user --no-cache-dir -r requirements.txt


# STAGE 2: Kiểm tra/Linter (Giai đoạn Testing)
FROM python:3.9-alpine AS tester
WORKDIR /app
COPY --from=builder /root/.local /root/.local
COPY . .
# Ở đây bạn có thể chạy lệnh test, nếu lỗi nó sẽ dừng build ngay
RUN python -m compileall . 


# STAGE 3: Image cuối cùng (Giai đoạn Runtime)
FROM python:3.9-alpine AS runner
WORKDIR /app

# Chỉ copy những thư viện đã cài đặt từ stage builder
COPY --from=builder /root/.local /root/.local
# Copy code nguồn
COPY app.py .

# Đảm bảo script trong .local có thể chạy được
ENV PATH=/root/.local/bin:$PATH

EXPOSE 5000
CMD ["python", "app.py"]