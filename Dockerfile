# Base Image
FROM amazoncorretto:17

# Image meta 정보
LABEL maintainer="up-data<ohd7150@gmail.com>"

# Build 시 사용할 변수 선언
ARG JAR_FILE_PATH=build/libs/*.jar

# 생성할 image의 / 디렉토리에 파일 복사
COPY ${JAR_FILE_PATH} /data.jar

# start.sh 스크립트 복사
COPY start.sh /usr/local/bin/start.sh

# start.sh 스크립트 실행 권한 부
RUN chmod +x /usr/local/bin/start.sh

# start.sh 스크립트를 파일 실행 -> java 파일 실행
ENTRYPOINT ["/usr/local/bin/start.sh"]



