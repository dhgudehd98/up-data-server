package com.sh.updown.chroling;



import com.sh.updown.data.InterparkList;
import com.sh.updown.dto.ProductDto;
import com.sh.updown.entity.Destination;
import com.sh.updown.entity.ProductInformation;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
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
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        System.out.println("ChromeDriver path set to: " + System.getProperty("webdriver.chrome.driver"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.setBinary("/usr/local/bin/headless-chrome");
//        options.addArguments("--headless"); // 브라우저 UI를 표시하지 않음 (headless 모드)
        WebDriver driver = new ChromeDriver(options);

        Destination[] destinations = Destination.values();
        try {
            for (int i = 0; i < url.size(); i++) {
                driver.get(url.get(i));
                Destination destination = destinations[i];

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.itemInfo > div.itemInfoTop > div.itemInfoMain > div.title")));

                String site = "인터파크";
                // 상품 요소 선택
                List<WebElement> items = driver.findElements(By.cssSelector("div.resultContent > ul.tourCompSearchList > li"));

                for (WebElement item : items) {
                    try {

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

                        String duration = "";

                        if (placeSpans.size() > 0) {
                            duration = placeSpans.get(0).getText().trim();
                        } else {
                            System.out.println("기간 정보가 없습니다.");
                        }

                        ProductInformation travelInformation = ProductInformation.builder()
                                .title(title) // 여행 제목
                                .nights(duration) // 몇박 몇일
                                .price(price) // 가격
                                .thumbnailUrl(imageUrl) //여행지 이미지
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
