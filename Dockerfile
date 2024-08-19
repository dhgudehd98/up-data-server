# Base Image
FROM amazoncorretto:17

# Image meta 정보
LABEL maintainer="up-data<ohd7150@gmail.com>"

# 필요한 패키지 및 Chrome, unzip 설치
RUN yum update -y && \
    yum install -y wget unzip curl google-chrome-stable && \
    ln -s /usr/bin/google-chrome /usr/bin/chrome && \
    yum clean all

# Chromedriver 다운로드 및 설치
COPY chromedriver-linux64.zip /tmp/
RUN unzip /tmp/chromedriver_linux64.zip -d /tmp/ && \
    mv /tmp/chromedriver /usr/local/bin/chromedriver && \
    chmod +x /usr/local/bin/chromedriver && \
    rm /tmp/chromedriver_linux64.zip
# JAR 파일 복사
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} /data.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/data.jar"]
