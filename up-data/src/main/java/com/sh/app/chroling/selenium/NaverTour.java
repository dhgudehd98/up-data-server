package com.sh.app.chroling.selenium;


import com.sh.app.chroling.entity.Product;
import com.sh.app.chroling.entity.ProductInformation;
import com.sh.app.chroling.repository.DataRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Component
@RequiredArgsConstructor
public class NaverTour  {
    private final DataRepository dataRepository;

    public static void main(String[] args)  {
        // ChromeDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");

        // ChromeDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless"); // 브라우저 UI를 표시하지 않음 (headless 모드)
        WebDriver driver = new ChromeDriver(options);

        String url = "https://pkgtour.naver.com/domestic-list?destination=14&departureDate=2024.08.03.%2C2024.08.17."; // 실제 URL로 변경
        driver.get(url);



        List<WebElement> items = driver.findElements(By.cssSelector("li.item.DomesticProduct"));
        for (WebElement item : items) {
            String site = "네이버 티켓 패키지";
            String title = item.findElement(By.cssSelector("b.name")).getText();
            String thumbnailUrl = item.findElement(By.cssSelector("img.img")).getAttribute("src");
            int price =Integer.valueOf(item.findElement(By.cssSelector("div.base strong.value")).getText().replaceAll("[^0-9]", "")); // 숫자만 추출
            String sellerSrc = item.findElement(By.cssSelector("div.agent img.logo")).getAttribute("src");
            // 날짜와 기간 추출
            List<WebElement> itemOptions = item.findElements(By.cssSelector("div.options span.option"));
            String area = itemOptions.size() > 0 ? itemOptions.get(0).getText() : ""; // 제주출발

            //출발 날짜
            String startDate = itemOptions.size() > 1 ? itemOptions.get(1).getText().replaceAll("[가-힣]","").replaceAll("\\.$",""): ""; // 2024.08.06.화
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate start_date = LocalDate.parse(startDate, formatter);
            //몇박 몇일인지 추가
            String nights = itemOptions.size() > 2 ? itemOptions.get(2).getText() : ""; // 2박 3일

            // <a class="anchor"> 링크 가져오기
            WebElement linkElement = item.findElement(By.cssSelector("a.anchor"));
            String detailUrl = linkElement.getAttribute("href");

            // TravelInformation 객체 생성
            ProductInformation travelInfo = ProductInformation.builder()
                    .title(title)
                    .nights(nights) // 여행 기간
                    .start_date(start_date) // 시작 날짜
                    .price(price) // 가격
                    .thumbnailUrl(thumbnailUrl) // 이미지 링크
                    .detailUrl(detailUrl)
                    .area(area)// 상세 링크
                    .build();

            // ChrolingData 객체 생성
            Product naverTour = Product.builder()
                    .sourceSite(site)
                    .productInformation(travelInfo)
                    .build();
            System.out.println(naverTour.toString());
            // 데이터 저장
//            dataRepository.save(naverTour);

            System.out.println(naverTour.toString() );
        }


        // 브라우저 닫기
        driver.quit();
    }
}
