# up-data-server
# 2024.08.06
### Task  
- 구축한 NCP 데이터 서버 연결 및 Spring boot 크롤링 작업 및 데이터 저장 
### Difficult
- Spring boot 2.7.12 버전과 호환되지 않는 Selenium 버전으로 인해서 크롤링 작업 실패 
### ErrorCode
- Websocket ContinueException 
### Solution 
- Jsoup으로 크롤링 작업 실행 
  - NaverTour에서는 Jsoup을 이용해서 크롤링 작업을 성공할 수 있었으나 InterPark에서는 코드를 정확히 입력했음에도 불구하고, 크롤링 작업 실패 
  - options.addArguments("--remote-allow-origins=*"); CORS 정책에 의해서 요청된 차단을 우회해서 ChromeDriver가 접속할 수 있도록 해주는 코드 