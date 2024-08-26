package com.up.down.batch.crawling;


import com.up.down.batch.crawling.data.NaverList;
import com.up.down.batch.common.dto.ProductDto;
import com.up.down.batch.common.entity.Destination;
import com.up.down.batch.common.entity.ProductInformation;
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
      NaverList naverList = new NaverList();

      //Enum 값을 가져오기 위해서 설정
      Destination[] destinations = Destination.values();
      //제주 , 강원, 전라, 부산, 거제, 남해, 통영, 경주, 여수, 울릉도

      //네이버 url 리스트 정보들 가져오기
      List<String> urlList = naverList.list();
      //반환 해야되는 리스트
      List<ProductDto> productList = new ArrayList<>();

      for(int i = 0; i<urlList.size(); i++) {
          Document doc = Jsoup.connect(urlList.get(i)).get();
          Destination destination = destinations[i % destinations.length];

          // 데이터 추출
          Elements items = doc.select("li.item.DomesticProduct");


          if (items.isEmpty()) {
              log.debug("패키지 여행 상품이 존재하지 않습니다.");
              continue;
          }
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
              String startDateData = itemOptions.size() > 1 ? itemOptions.get(1).text().replaceAll("[가-힣]", "").replaceAll("\\.$", "") : ""; // 2024.08.06.화
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
              LocalDate startDate = LocalDate.parse(startDateData, formatter);

              // 몇박 몇일인지 추가
              String nightsString = itemOptions.size() > 2 ? itemOptions.get(2).text() : ""; // 2박 3일
              int nights;
              try {
                  nights = Integer.parseInt(nightsString.charAt(0) + "");
              } catch(Exception e) {
                  nights = 0;
              }

              // <a class="anchor"> 링크 가져오기
              String detailUrl = item.selectFirst("a.anchor").attr("href");

              ProductInformation information = ProductInformation.builder()
                      .title(title)
                      .nights(nights) // 여행 기간
                      .startDate(startDate) // 시작 날짜
                      .price(price) // 가격
                      .thumbnailUrl(thumbnailUrl) // 이미지 링크
                      .detailUrl(detailUrl)
                      .travelAgency(sellerSrc)
                      .destination(destination)
                      .build();

              ProductDto naverTour = ProductDto.builder()
                      .sourceSite(site)
                      .productInformationDto(information)
                      .build();

              productList.add(naverTour);
          }
      }
        return productList;
    }
}
