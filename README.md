# up-data-server 
### 🚀[기본 어플리케이션 'up-and-down'](https://github.com/ssg-java3-240304/up-and-down)

## 👀 up-and-down, up-data-server Architecture
![](https://kr.object.ncloudstorage.com/up-bucket/Wiki/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-08-29%20%EC%98%A4%ED%9B%84%205.42.30.png)

# 🔧 수행 도구

**programming** : ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![HTML](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)

**framework** :
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)

**DB** : 
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

**ORM** :
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)

**협업툴** : 
![Slack](https://img.shields.io/badge/slack-%234A154B.svg?style=for-the-badge&logo=slack&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)

**디자인** :
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)

**기타** : ![ElasticSearch](https://img.shields.io/badge/-ElasticSearch-005571?style=for-the-badge&logo=elasticsearch)
![Spring Batch](https://img.shields.io/badge/Spring%20Batch-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)


# 개발 과정 오류 발생

## 1. Scheduler를 통해서 실행 할 때 작동 X


### 🏃‍♀️ Try 🏃

**ScheduilingJobConfiguration**
```
package com.sh.updown.batch.job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulingJobConfiguration {

    private final JobLauncher jobLauncher;
    private final Job mySpringBatchJob;  // Spring Batch Job 주입

    @Scheduled(cron = "*/15 * * * * ?")
    public void scheduleJobLauncher() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())  // 고유한 파라미터 추가
                .addDate("runDate", new Date())  // 추가 고유 파라미터
                .toJobParameters();

        log.info("Job 인스턴스: {}", mySpringBatchJob);

        jobLauncher.run(mySpringBatchJob, jobParameters);
    }
}
```
**SpringBatchJob**

    package com.sh.updown.batch.job;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.Step;
    import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
    import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
    import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
    import org.springframework.batch.core.launch.support.RunIdIncrementer;
    import org.springframework.batch.item.ItemProcessor;
    import org.springframework.batch.item.ItemReader;
    import org.springframework.batch.item.ItemWriter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    
    import java.util.Arrays;
    import java.util.List;
    
    @Configuration
    @Slf4j
    public class SpringBatchJob {
    
        private final JobBuilderFactory jobBuilderFactory;
        private final StepBuilderFactory stepBuilderFactory;
    
        public SpringBatchJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
            this.jobBuilderFactory = jobBuilderFactory;
            this.stepBuilderFactory = stepBuilderFactory;
        }
    
        @Bean
        public Job mySpringBatchJob(){
            return jobBuilderFactory.get("mySpringBatchJob")
                    .incrementer(new RunIdIncrementer())
                    .start(step1())
                    .next(step2())
                    .build();
        }
    
        @Bean
        public Step step1(){
            return stepBuilderFactory.get("step1")
                    .<String, String>chunk(5)
                    .reader(itemReader())
                    .processor(itemProcessor())
                    .writer(itemWriter())
                    .build();
        }
    
        @Bean
        public Step step2() {
            return stepBuilderFactory.get("step2")
                    .<String, String>chunk(1)
                    .reader(itemReader2())  // 다른 Reader, Processor, Writer를 사용할 수 있습니다.
                    .processor(itemProcessor2())
                    .writer(itemWriter2())
                    .build();
        }
    
        @Bean
        public ItemReader<String> itemReader() {
            log.debug("==== itemReader1 시작 ====");
            return new ItemReader<>() {
                private final List<String> data = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4", "Item 5");
                private int index = 0;
    
                @Override
                public String read() {
                    if (index < data.size()) {
                        return data.get(index++);
                    }
                    return null;  // null을 반환하면 Reader는 더 이상 읽을 데이터가 없다고 간주합니다.
                }
            };
        }
    
        @Bean
        public ItemProcessor<String, String> itemProcessor() {
            log.debug("==== itemProcessor1 시작 ====");
            return item -> {
                log.info("Processing item: {}", item);
                return item.toUpperCase();  // 예시로 데이터를 대문자로 변환
            };
        }
    
        @Bean
        public ItemWriter<String> itemWriter() {
            log.debug("==== itemWriter1 시작 ====");
            return items -> {
                for (String item : items) {
                    log.info("Writing item: {}", item);
                }
            };
        }
    
        // Step2를 위한 별도의 Reader, Processor, Writer를 정의할 수도 있습니다.
        @Bean
        public ItemReader<String> itemReader2() {
            log.debug("==== itemReader2 시작 ====");
            return new ItemReader<>() {
                private final List<String> data = Arrays.asList("Another Item 1", "Another Item 2", "Another Item 3");
                private int index = 0;
    
                @Override
                public String read() {
                    if (index < data.size()) {
                        return data.get(index++);
                    }
                    return null;
                }
            };
        }
    
        @Bean
        public ItemProcessor<String, String> itemProcessor2() {
            log.debug("==== itemProcessor2 시작 ====");
            return item -> {
                log.info("=====Processing item in step2: {}=====", item);
                return item.toLowerCase();  // 예시로 데이터를 소문자로 변환
            };
        }
    
        @Bean
        public ItemWriter<String> itemWriter2() {
            log.debug("==== itemWriter2 시작 ====");
            return items -> {
                for (String item : items) {
                    log.info("=====Writing item in step2: {}=====", item);
                }
            };
        }
    }

### 🏁 Solution 🏁
**@StepScope 설정하지 않음.**

**@StepScope를 설정이 필요한 경우**
1. JobParameters를 사용하는 경우
   - ItemReader, ItemProcessor, ItemWriter에서 JobParameters를 사용해야 한다면, 해당 빈들이 배치 잡이 실행 될 때 동적으로 생성되어야한다. 그렇지 않으면 JobParameter를 정상적으로 참조하지 못함.
2. 동적 파라미터를 사용하는 경우
  - 배치 작업은 실행 시점에 다양한 파라미터(Job Parameter)를 받아 처리해야 한다. @StepScope를 사용하면 스텝 실행 시점에 파라미터를 주입할 수 있어, 동일한 배치 잡을 여러번 실행 할 때 각 실행마다 다른 파라미터를 사용할 수 있ㄷ다.

## 2. Docker 이미지에서 Chrome 설치 과정에서 에러 발생 

### 🏃‍♀️ Try 🏃

**Dockerfile**
```
# 기본 이미지 설정
FROM amazoncorretto:17

# 유지 관리자 설정
LABEL maintainer="up-data<ohd7150@gmail.com>"

ARG JAR_FILE_PATH=build/libs/*.jar

# 파일 복사
COPY ${JAR_FILE_PATH} /data.jar
COPY start.sh /usr/local/bin/start.sh

RUN chmod +x /usr/local/bin/start.sh \
    && yum update -y \
    && yum install -y wget unzip atk dbus-libs libX11 libXcomposite libXcursor libXdamage libXext libXi libXrandr libXtst libXss cups-libs dbus-glib GConf2 libxcb at-spi2-atk

# 엔트리 포인트 설정
ENTRYPOINT ["/usr/local/bin/start.sh"]

```
**start.sh**
```
#!/bin/bash
yum update -y

# 필요한 패키지 설치 (예: wget, unzip)
yum install -y wget unzip

# Chrome 다운로드 및 설치

# rpm으로 설치
wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm
yum install -y ./google-chrome-stable_current_x86_64.rpm

# Chromedriver 설치
wget https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.119/linux64/chromedriver-linux64.zip
unzip chromedriver-linux64.zip
mv chromedriver-linux64/chromedriver /usr/local/bin/chromedriver
chmod +x /usr/local/bin/chromedriver
# Chromedriver 설치 확인
if [ -f /usr/local/bin/chromedriver ]; then
    echo "Chromedriver installed successfully."
else
    echo "Chromedriver installation failed."
    exit 1

# 애플리케이션 실행
echo "Starting application..."
exec java -jar /data.jar
```

**에러코드**
```
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.batch.item.ItemReader]: Factory method 'interParkReader' threw exception; nested exception is org.openqa.selenium.SessionNotCreatedException: Could not start a new session. Response code 500. Message: session not created: Chrome failed to start: exited normally.
  (session not created: DevToolsActivePort file doesn't exist)
  (The process started from chrome location /usr/bin/google-chrome is no longer running, so ChromeDriver is assuming that Chrome has crashed.)
Build info: version: '4.1.4', revision: '535d840ee2'
System info: host: '21c485367069', ip: '172.17.0.3', os.name: 'Linux', os.arch: 'amd64', os.version: '4.18.0-477.27.1.el8_8.x86_64', java.version: '17.0.12'
Driver info: org.openqa.selenium.chrome.ChromeDriver
Command: [null, newSession {capabilities=[Capabilities {browserName: chrome, goog:chromeOptions: {args: [--no-sandbox, --disable-dev-shm-usage, --enable-gpu, --disable-setuid-sandbox, --headless, --remote-allow-origins=*], binary: /usr/bin/google-chrome, extensions: []}}], desiredCapabilities=Capabilities {browserName: chrome, goog:chromeOptions: {args: [--no-sandbox, --disable-dev-shm-usage, --enable-gpu, --disable-setuid-sandbox, --headless, --remote-allow-origins=*], binary: /usr/bin/google-chrome, extensions: []}}}]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:185) ~[spring-beans-5.3.27.jar!/:5.3.27]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653) ~[spring-beans-5.3.27.jar!/:5.3.27]
	... 41 common frames omitted
Caused by: org.openqa.selenium.SessionNotCreatedException: Could not start a new session. Response code 500. Message: session not created: Chrome failed to start: exited normally.
  (session not created: DevToolsActivePort file doesn't exist)
  (The process started from chrome location /usr/bin/google-chrome is no longer running, so ChromeDriver is assuming that Chrome has crashed.)
Build info: version: '4.1.4', revision: '535d840ee2'
System info: host: '21c485367069', ip: '172.17.0.3', os.name: 'Linux', os.arch: 'amd64', os.version: '4.18.0-477.27.1.el8_8.x86_64', java.version: '17.0.12'
Driver info: org.openqa.selenium.chrome.ChromeDriver
```
-> 이 에러코드는 Chrome과 Chromedriver의 버전이 동일하지 않은 경우 발생하는 에러
버전을 확인하기 위해서 chromedriver --version, google-chrome --version을 확인 해봤지만 chromedrvier --version의 버전은 출력됐지만, google-chrome은 version이 출력되지 않은것을 확인.
google-chrome이 설치되지 않은것으로 확인.


### 🏁 Solution 🏁
DockerFile의 배포이미지를 FROM amazoncorretto:17에서 FROM openjdk:17.0.1-jdk-slim 변경하여 해결 

**DockerFile**
```
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
```
