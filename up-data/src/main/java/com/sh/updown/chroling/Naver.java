package com.sh.updown.chroling;


import com.sh.updown.dto.ProductDto;
import com.sh.updown.entity.ProductInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class Naver {

  public List<ProductDto> naverChroling() throws IOException {
      String url = "https://pkgtour.naver.com/domestic-list?destination=14&departureDate=2024.08.03.%2C2024.08.17."; // 실제 URL로 변경
      Document doc = Jsoup.connect(url).get();

      List<ProductDto> productList = new ArrayList<>();
        // 데이터 추출
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

           ProductInformation information = ProductInformation.builder()
                   .title(title)
                   .nights(nights) // 여행 기간
                   .start_date(start_date) // 시작 날짜
                   .price(price) // 가격
                   .thumbnailUrl(thumbnailUrl) // 이미지 링크
                   .detailUrl(detailUrl)
                   .travelAgency(sellerSrc)
                   .area(area) // 상세 링크
                   .build();

            ProductDto naverTour = ProductDto.builder()
                    .sourceSite(site)
                    .productInformationDto(information)
                    .build();


            log.debug("{}", naverTour.toString());
            productList.add(naverTour);
        }

        return productList;
    }
}
