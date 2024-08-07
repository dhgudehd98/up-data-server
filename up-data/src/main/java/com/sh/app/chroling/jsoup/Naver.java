package com.sh.app.chroling.jsoup;




import com.sh.app.chroling.entity.Product;
import com.sh.app.chroling.entity.ProductInformation;
import com.sh.app.chroling.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class Naver  {

    private final DataRepository dataRepository;

   public static void main(String[] args) throws IOException {
        String url = "https://pkgtour.naver.com/domestic-list?destination=14&departureDate=2024.08.03.%2C2024.08.17."; // 실제 URL로 변경

        // HTTP 요청을 보내고 HTML을 가져옵니다.
        Document doc = Jsoup.connect(url).get();

        // 페이지에서 필요한 데이터를 추출합니다.
        Elements items = doc.select("li.item.DomesticProduct");
        for (Element item : items) {
            String site = "네이버 티켓 패키지";
            String title = item.selectFirst("b.name").text();
            String thumbnailUrl = item.selectFirst("img.img").attr("src");
            int price = Integer.parseInt(item.selectFirst("div.base strong.value").text().replaceAll("[^0-9]", "")); // 숫자만 추출
            String sellerSrc = item.selectFirst("div.agent img.logo").attr("src");
            // 날짜와 기간 추출
            Elements itemOptions = item.select("div.options span.option");
            String area = itemOptions.size() > 0 ? itemOptions.get(0).text() : ""; // 제주출발

            //출발 날짜
            String startDate = itemOptions.size() > 1 ? itemOptions.get(1).text().replaceAll("[가-힣]","").replaceAll("\\.$","") : ""; // 2024.08.06.화
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate start_date = LocalDate.parse(startDate, formatter);

            // 몇박 몇일인지 추가
            String nights = itemOptions.size() > 2 ? itemOptions.get(2).text() : ""; // 2박 3일

            // <a class="anchor"> 링크 가져오기
            String detailUrl = item.selectFirst("a.anchor").attr("href");

//             TravelInformation 객체 생성
            ProductInformation travelInfo = ProductInformation.builder()
                    .title(title)
                    .nights(nights) // 여행 기간
                    .start_date(start_date) // 시작 날짜
                    .price(price) // 가격
                    .thumbnailUrl(thumbnailUrl) // 이미지 링크
                    .detailUrl(detailUrl)
                    .travelAgency(sellerSrc)
                    .area(area) // 상세 링크
                    .build();

            // ChrolingData 객체 생성
            Product naverTour = Product.builder()
                    .sourceSite(site)
                    .productInformation(travelInfo)
                    .build();
            System.out.println(naverTour.toString());

            // 데이터 저장
            //dataRepository.save(naverTour);
            System.out.println(sellerSrc);
        }
    }
}
