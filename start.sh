#!/bin/bash
yum update -y

# 필요한 패키지 설치 (예: wget, unzip)
yum install -y wget unzip

yum install -y atk
# 필요한 패키지 설치 (예: wget, unzip, libdbus)
yum install -y wget unzip atk dbus-libs libX11 libXcomposite libXcursor libXdamage libXext libXi libXrandr libXtst libXss cups-libs dbus-glib GConf2

# Chrome 다운로드 및 설치
echo "Downloading and installing Chrome..."
echo "Adding Google Chrome repository..."
cat <<EOF > /etc/yum.repos.d/google-chrome.repo
[google-chrome]
name=google-chrome - \$basearch
baseurl=http://dl.google.com/linux/chrome/rpm/stable/\$basearch
enabled=1
gpgcheck=1
gpgkey=https://dl.google.com/linux/linux_signing_key.pub
EOF
# Google Chrome 설치
echo "Installing Google Chrome..."
yum install -y google-chrome-stable
# 설치 완료 알림
echo "Chrome setup complete."

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