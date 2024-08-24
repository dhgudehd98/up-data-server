# 기본 이미지 설정
FROM amazoncorretto:17

# 유지 관리자 설정
LABEL maintainer="up-data<ohd7150@gmail.com>"

ARG JAR_FILE_PATH=build/libs/*.jar

# 파일 복사
COPY ${JAR_FILE_PATH} /data.jar
COPY start.sh /usr/local/bin/start.sh

# 엔트리 포인트 설정
ENTRYPOINT ["/usr/local/bin/start.sh"]
