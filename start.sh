#!/bin/bash
yum update -y

# 필요한 패키지 설치 (예: wget, unzip)
yum install -y wget unzip

yum install -y atk
# 필요한 패키지 설치 (예: wget, unzip, libdbus)
yum install -y wget unzip atk dbus-libs libX11 libXcomposite libXcursor libXdamage libXext libXi libXrandr libXtst libXss cups-libs dbus-glib GConf2


# rpm으로 설치
#wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm
#yum install -y ./google-chrome-stable_current_x86_64.rpm

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