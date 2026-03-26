import os

app_env = os.environ.get('APP_ENV', 'unknown')
print(f"Ứng dụng đang chạy trong môi trường: {app_env}")
