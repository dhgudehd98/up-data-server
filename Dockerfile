# 기본 이미지 설정
FROM amazoncorretto:17

# 유지 관리자 설정
LABEL maintainer="up-data<ohd7150@gmail.com>"

ARG JAR_FILE_PATH=build/libs/*.jar

# 파일 복사
COPY ${JAR_FILE_PATH} /data.jar
COPY start.sh /usr/local/bin/start.sh

# 환경 변수 설정
ENV LD_LIBRARY_PATH=/usr/lib64:$LD_LIBRARY_PATH

# Google Chrome 리포지토리 추가 및 설치
# Google Chrome 리포지토리 추가 및 설치
# Google Chrome 리포지토리 추가
RUN echo "Adding Google Chrome repository..." \
    && cat <<EOF > /etc/yum.repos.d/google-chrome.repo
[google-chrome]
name=google-chrome - \$basearch
baseurl=http://dl.google.com/linux/chrome/rpm/stable/\$basearch
enabled=1
gpgcheck=1
gpgkey=https://dl.google.com/linux/linux_signing_key.pub
EOF
# Google Chrome 설치
RUN yum install -y google-chrome-stable
# 스크립트 실행 권한 부여 및 필요한 패키지 설치
RUN chmod +x /usr/local/bin/start.sh \
    && yum update -y \
    && yum install -y wget unzip atk dbus-libs libX11 libXcomposite libXcursor libXdamage libXext libXi libXrandr libXtst libXss cups-libs dbus-glib GConf2 libxcb at-spi2-atk

# 엔트리 포인트 설정
ENTRYPOINT ["/usr/local/bin/start.sh"]
