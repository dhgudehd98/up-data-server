# 기본 이미지 설정
FROM amazoncorretto:17
# 유지 관리자 설정
LABEL maintainer="up-data<ohd7150@gmail.com>"

RUN yum install -y wget unzip

#크롬 설치
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm
RUN yum install -y ./google-chrome-stable_current_x86_64.rpm

RUN wget https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.119/linux64/chromedriver-linux64.zip

# 크롬 드라이버 설치
RUN unzip chromedriver-linux64.zip
RUN mv chromedriver-linux64/chromedriver /usr/local/bin/chromedriver
RUN chmod +x /usr/local/bin/chromedriver

# 파일 환경 변수 설정
ARG JAR_FILE_PATH=build/libs/*.jar
# 파일 복사
COPY ${JAR_FILE_PATH} /data.jar

# 엔트리 포인트 설정
ENTRYPOINT ["java", "-jar", "/data.jar"]