package com.up.down.batch.crawling;



import com.up.down.batch.crawling.data.InterparkList;
import com.up.down.batch.common.dto.ProductDto;
import com.up.down.batch.common.entity.Destination;
import com.up.down.batch.common.entity.ProductInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class Interpark {

    //InterPark url을 가지고 있는 객체 선언
    InterparkList list = new InterparkList();
    // interpark url을 가지고 있는 list 설정
    List<String> url = list.list();

    List<ProductDto> interparkList = new ArrayList<>();
    public List<ProductDto> interparkChroling() throws Exception {

        //ChromeDriver 옵션 설정 및 연결
        //로컬 환경에서의 Chromedriver 실행
//        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");

        //서버에서 chromedriver 구축
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        options.setBinary("/usr/bin/google-chrome");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox"); // 추가한 옵션
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu"); //추가한 옵션
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--remote-allow-origins=*"); // 원격 연결 허용
        WebDriver driver = new ChromeDriver(options);

        Destination[] destinations = Destination.values();
        try {
            for (int i = 0; i < url.size(); i++) {
                driver.get(url.get(i));
                Destination destination = destinations[i];

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.itemInfo > div.itemInfoTop > div.itemInfoMain > div.title")));

                // 상품 요소 선택
                List<WebElement> items = driver.findElements(By.cssSelector("div.resultContent > ul.tourCompSearchList > li"));

                for (WebElement item : items) {
                    try {

                        String site = "인터파크";
                        // 상세 링크
                        WebElement linkElement = item.findElement(By.cssSelector("a"));
                        String detailLink = linkElement.getAttribute("href");

                        // 이미지 URL
                        WebElement imageElement = item.findElement(By.cssSelector("img"));
                        String imageUrl = imageElement.getAttribute("src");

                        // 제목
                        WebElement titleElement = item.findElement(By.cssSelector("div.itemInfo > div.itemInfoTop > div.itemInfoMain > div.title"));
                        String title = titleElement.getText().trim();

                        // 가격
                        WebElement priceElement = item.findElement(By.cssSelector("div.itemInfoPrice > div.final > strong"));
                        int price = Integer.valueOf(priceElement.getText().replaceAll("[^0-9]", "")); // 숫자만 추출

                        // 몇 박 몇 일 및 출발 지역
                        WebElement placeElement = item.findElement(By.cssSelector("div.itemInfoMain > div.place"));
                        List<WebElement> placeSpans = placeElement.findElements(By.tagName("span"));

                        // 시작 날짜
                        LocalDate startDate = null;
                        try {
                            // URL 패턴에서 날짜 추출하기
                            // https://travel.interpark.com/tour/goods?goodsCd=24090430684
                            String[] urlSplit = detailLink.split("goodsCd="); // "goodsCd="를 기준으로 URL을 분할
                            if (urlSplit.length > 1) {
                                String dateData = urlSplit[1]; // "goodsCd=" 뒤의 값을 추출
                                if (dateData.length() >= 6) {
                                    // dateData 에서 날짜로 사용될 수 있는 6자리 추출 (예: 240904)
                                    String potentialDate = dateData.substring(0, 6);
                                    // 날짜 형식 유효성 검사 (예: "20230826" 형태로 변환)
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
                                    try {
                                        startDate = LocalDate.parse(potentialDate, formatter); // LocalDate로 변환
                                    } catch (DateTimeParseException e) {
                                        // 만약 parsing에 실패하면 null로 유지
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String duration = "";
                        int nights;
                        if (placeSpans.size() > 0) {
                            duration = placeSpans.get(0).getText().trim();
                            nights = Integer.parseInt(duration.charAt(0) + "");
                        } else {
                           nights = 0;
                        }

                        ProductInformation travelInformation = ProductInformation.builder()
                                .title(title) // 여행 제목
                                .nights(nights) // 몇박 몇일
                                .startDate(startDate) // 시작 날짜
                                .price(price) // 가격
                                .thumbnailUrl(imageUrl) //여행지 이미지
                                .travelAgency(site)
                                .detailUrl(detailLink) // 상품 상세페이지
                                .destination(destination)
                                .build();
                        //인터파크에는 시작 날짜랑 여행일 존재하지 않음 -> 몇박 몇일인지만 존재
                        ProductDto chrolingData = ProductDto.builder()
                                .sourceSite(site)
                                .productInformationDto(travelInformation)
                                .build();
                        interparkList.add(chrolingData);


                    } catch (Exception e) {
                        // 오류가 발생하면 출력
                        e.printStackTrace();
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            // 브라우저 종료
            driver.quit();
        }

        return interparkList;
    }
}
