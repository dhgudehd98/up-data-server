# 기본 이미지 설정
FROM openjdk:17.0.1-jdk-slim
# 유지 관리자 설정
LABEL maintainer="up-data<ohd7150@gmail.com>"

RUN apt-get -y update

RUN apt -y install wget

RUN apt -y install unzip

RUN apt -y install curl

RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb

RUN apt -y install ./google-chrome-stable_current_amd64.deb

RUN wget https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.119/linux64/chromedriver-linux64.zip

RUN unzip chromedriver-linux64.zip

RUN mv chromedriver-linux64/chromedriver /usr/local/bin/chromedriver

RUN chmod +x /usr/local/bin/chromedriver

# 파일 환경 변수 설정
ARG JAR_FILE_PATH=build/libs/*.jar
# 파일 복사
COPY ${JAR_FILE_PATH} /data.jar
# 엔트리 포인트 설정
ENTRYPOINT ["java", "-jar", "/data.jar"]
