#!/bin/bash
yum update -y

# 필요한 패키지 설치 (예: wget, unzip)
yum install -y wget unzip

# Chrome Headless Shell 다운로드 및설치
#yum install -y dbus-libs
echo "Downloading and installing Chrome Headless Shell..."
#wget https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.119/linux64/chrome-headless-shell-linux64.zip
#unzip chrome-headless-shell-linux64.zip -d /usr/local/bin/
yum install -y atk
# 필요한 패키지 설치 (예: wget, unzip, libdbus)
yum install -y wget unzip atk dbus-libs libX11 libXcomposite libXcursor libXdamage libXext libXi libXrandr libXtst libXss cups-libs dbus-glib GConf2

# Chrome 다운로드 및 설치
echo "Downloading and installing Chrome..."
wget https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.119/linux64/chrome-linux64.zip
unzip  chrome-linux64.zip -d /usr/local/bin/
# 설치 완료 알림
echo "Chrome Headless Shell setup complete."
unzip chrome-linux64.zip -d /usr/local/bin/

# 설치 완료 알림
echo "Chrome setup complete."

# Chromedriver 설치
echo "Downloading and installing Chromedriver..."
yum install -y libxcb
wget https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.119/linux64/chromedriver-linux64.zip
unzip chromedriver-linux64.zip
mv chromedriver-linux64/chromedriver /usr/local/bin/chromedriver
chmod +x /usr/local/bin/chromedriver
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