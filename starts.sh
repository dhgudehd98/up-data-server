sudo yum update -y

# 필요한 패키지 설치 (예: wget, unzip)
sudo yum install -y wget unzip

# Chrome Headless Shell 다운로드 및 설치
wget https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.119/linux64/chrome-headless-shell-linux64.zip
unzip chrome-headless-shell-linux64.zip -d /usr/local/bin/

# 설치 완료 알림
echo "Chrome Headless Shell setup complete."

# Chromedriver 설치
wget https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.119/linux64/chromedriver-linux64.zip
unzip chromedriver-linux64.zip
sudo mv chromedriver-linux64/chromedriver /usr/local/bin/chromedriver
sudo chmod +x /usr/local/bin/chromedriver
exec java -jar /data.jar