# Base Image
FROM amazoncorretto:17

# Maintainer
LABEL maintainer="up-data<ohd7150@gmail.com>"

# 스크립트 파일 복사
COPY setup.sh /setup.sh

# 스크립트 실행 권한 부여
RUN chmod +x /setup.sh

# 스크립트 실행
RUN /setup.sh

# JAR 파일 복사 (애플리케이션 실행을 위해)
COPY build/libs/*.jar /data.jar

# 컨테이너 시작 시 애플리케이션 실행
CMD ["java", "-jar", "/data.jar"]
