package com.sh.app.chroling.selenium;



import com.sh.app.chroling.entity.Product;
import com.sh.app.chroling.entity.ProductInformation;
import com.sh.app.chroling.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InterparkBySelenium implements CommandLineRunner{


    private final DataRepository dataRepository;


    @Override
    public void run(String... args) throws Exception {
        // WebDriverManager를 사용하여 ChromeDriver 설정
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");

        // ChromeDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless"); // 브라우저 UI를 표시하지 않음 (headless 모드)
        WebDriver driver = new ChromeDriver(options);

        try {
            // 웹 페이지 열기
            driver.get("https://travel.interpark.com/tour/search?category=domestic&q=%EC%A0%9C%EC%A3%BC%EB%8F%84&domain=t&startDate=20240814&endDate=&departure=%EC%B6%9C%EB%B0%9C%EC%A7%80+%EC%A0%84%EC%B2%B4");


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
                    String departureLocation = "";

                    if (placeSpans.size() > 0) {
                        duration = placeSpans.get(0).getText().trim();
                    } else {
                        System.out.println("기간 정보가 없습니다.");
                    }

                    if (placeSpans.size() > 1) {
                        departureLocation = placeSpans.get(1).getText().trim();
                    } else {
                        System.out.println("출발 지역 정보가 없습니다.");
                    }

                    //제목 HTML 구조 : div.itemInfo > div.itemInfoTop >  div.itemInfoMain > div.title
                    // 출력
//                    System.out.println("제목: " + title);
//                    System.out.println("가격: " + price);
//                    System.out.println("이미지 URL: " + imageUrl);
//                    System.out.println("상세 링크: " + detailLink);
//                    System.out.println("몇 박 몇 일: " + duration);
//                    System.out.println("출발 지역: " + departureLocation);
//                    System.out.println("------------");

                    ProductInformation travelInformation = ProductInformation.builder()
                            .title(title) // 여행 제목
                            .nights(duration) // 몇박 몇일
                            .price(price) // 가격
                            .thumbnailUrl(imageUrl) //여행지 이미지
                            .detailUrl(detailLink) // 상품 상세페이지
                            .area(departureLocation) // 출발지역
                            .build();
                    //인터파크에는 시작 날짜랑 여행일 존재하지 않음 -> 몇박 몇일인지만 존재
                    Product chrolingData = Product.builder()
                            .sourceSite(site)
                            .productInformation(travelInformation)
                            .build();

                    dataRepository.save(chrolingData);
                } catch (Exception e) {
                    // 오류가 발생하면 출력
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 브라우저 종료
            driver.quit();
        }
    }
}
