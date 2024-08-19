# Base Image
FROM amazoncorretto:17

# Image meta 정보
LABEL maintainer="up-data<ohd7150@gmail.com>"

# 필요한 패키지 설치
RUN yum update -y && \
    yum install -y wget unzip atk dbus-libs libX11 libXcomposite libXcursor libXdamage libXext libXi libXrandr libXtst libXss cups-libs dbus-glib GConf2 && \
    wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm && \
    yum localinstall -y google-chrome-stable_current_x86_64.rpm && \
    rm -f google-chrome-stable_current_x86_64.rpm

# Chromedriver 다운로드 및 설치
RUN wget -q https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.119/linux64/chromedriver-linux64.zip && \
    unzip chromedriver-linux64.zip -d /usr/local/bin/ && \
    rm -f chromedriver-linux64.zip && \
    chmod +x /usr/local/bin/chromedriver

# 환경 변수 설정
ENV PATH="/usr/local/bin/chromedriver:${PATH}"

# JAR 파일 복사
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} /data.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/data.jar"]
