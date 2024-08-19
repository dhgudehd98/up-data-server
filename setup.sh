#!/bin/bash

# 스크립트 실행 시작 알림
echo "Starting setup..."

# 패키지 업데이트
yum update -y

# 필요한 패키지 설치
yum install -y wget unzip atk dbus-libs libX11 libXcomposite libXcursor libXdamage libXext libXi libXrandr libXtst libXss cups-libs dbus-glib GConf2

# Chrome 다운로드 및 설치
echo "Downloading and installing Chrome..."
wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm
yum localinstall -y google-chrome-stable_current_x86_64.rpm

# 설치 완료 알림
echo "Chrome setup complete."

# Chromedriver 설치
echo "Downloading and installing Chromedriver..."
wget https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.119/linux64/chromedriver-linux64.zip
unzip chromedriver-linux64.zip -d /usr/local/bin/

# Chromedriver 설치 확인
if [ -f /usr/local/bin/chromedriver ]; then
    echo "Chromedriver installed successfully."
else
    echo "Chromedriver installation failed."
    exit 1
fi

# 애플리케이션 실행
echo "Starting application..."
exec java -jar /data.jar
