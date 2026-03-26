# Stage 1: Build
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy toàn bộ thư mục hiện tại vào /app trong container
COPY . .

# Biên dịch tất cả file .java (bất kể nằm trong folder nào) vào thẳng thư mục /app
RUN find . -name "*.java" -exec javac -d . {} +

# Stage 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Chỉ copy các file .class từ stage build sang
COPY --from=build /app/*.class .

# Kiểm tra xem file có thực sự tồn tại không (để debug nếu vẫn lỗi)
RUN ls -l

CMD ["java", "HelloWorld"]