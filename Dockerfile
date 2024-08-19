# Base Image
FROM amazoncorretto:17

# Maintainer
LABEL maintainer="up-data<ohd7150@gmail.com>"

# 생성할 image의 / 디렉토리에 파일 복사
COPY ${JAR_FILE_PATH} /data.jar

# Container 구동 시 실행할 명령어
ENTRYPOINT ["java", "-jar", "/data.jar"]
# start.sh 스크립트 복사
COPY start.sh /usr/local/bin/start.sh

# start.sh 스크립트 실행 권한 부여
RUN chmod +x /usr/local/bin/start.sh

# start.sh 스크립트를 ENTRYPOINT로 설정
ENTRYPOINT ["/usr/local/bin/start.sh"]