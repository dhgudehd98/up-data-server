# 기본 이미지 설정
FROM amazoncorretto:17

# 유지 관리자 설정
LABEL maintainer="up-data<ohd7150@gmail.com>"

# 파일 복사
COPY ${JAR_FILE_PATH} /data.jar
COPY start.sh /usr/local/bin/start.sh

# 스크립트 실행 권한 부여 및 패키지 설치 명령어 추가
RUN chmod +x /usr/local/bin/start.sh \
    && yum update -y \
    && yum install -y wget unzip atk dbus-libs libX11 libXcomposite libXcursor libXdamage libXext libXi libXrandr libXtst libXss cups-libs dbus-glib GConf2 libxcb

# 엔트리 포인트 설정
ENTRYPOINT ["/usr/local/bin/start.sh"]
