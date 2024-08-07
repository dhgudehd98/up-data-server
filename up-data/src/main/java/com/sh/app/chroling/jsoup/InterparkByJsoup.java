package com.sh.app.chroling.jsoup;



import com.sh.app.chroling.entity.Product;
import com.sh.app.chroling.entity.ProductInformation;
import com.sh.app.chroling.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterparkByJsoup  {

    private final DataRepository dataRepository;

   public static void main(String[] args){
        String url = "https://travel.interpark.com/tour/search?category=domestic&q=%EC%A0%9C%EC%A3%BC%EB%8F%84&domain=t&startDate=20240814&endDate=&departure=%EC%B6%9C%EB%B0%9C%EC%A7%80+%EC%A0%84%EC%B2%B4";
        String site = "인터파크";

        try {
            // 웹 페이지 열기
            Document document = Jsoup.connect(url).get();

            // 상품 요소 선택
            Elements items = document.select("div.resultContent > ul.tourCompSearchList > li");

            for (Element item : items) {
                try {
                    // 상세 링크
                    Element linkElement = item.selectFirst("a");
                    String detailLink = linkElement.attr("href");

                    // 이미지 URL
                    Element imageElement = item.selectFirst("img");
                    String imageUrl = imageElement.attr("src");

                    // 제목
                    Element titleElement = item.selectFirst("div.itemInfo > div.itemInfoTop > div.itemInfoMain > div.title");
                    String title = titleElement.text().trim();

                    // 가격
                    Element priceElement = item.selectFirst("div.itemInfoPrice > div.final > strong");
                    int price = Integer.parseInt(priceElement.text().replaceAll("[^0-9]", "")); // 숫자만 추출

                    // 몇 박 몇 일 및 출발 지역
                    Element placeElement = item.selectFirst("div.itemInfoMain > div.place");
                    Elements placeSpans = placeElement.select("span");

                    String duration = "";
                    String departureLocation = "";

                    if (placeSpans.size() > 0) {
                        duration = placeSpans.get(0).text().trim();
                    } else {
                        System.out.println("기간 정보가 없습니다.");
                    }

                    if (placeSpans.size() > 1) {
                        departureLocation = placeSpans.get(1).text().trim();
                    } else {
                        System.out.println("출발 지역 정보가 없습니다.");
                    }

                    ProductInformation travelInformation = ProductInformation.builder()
                            .title(title) // 여행 제목
                            .nights(duration) // 몇박 몇일
                            .price(price) // 가격
                            .thumbnailUrl(imageUrl) //여행지 이미지
                            .detailUrl(detailLink) // 상품 상세페이지
                            .area(departureLocation) // 출발지역
                            .build();

                    Product chrolingData = Product.builder()
                            .sourceSite(site)
                            .productInformation(travelInformation)
                            .build();

                    System.out.println(chrolingData.toString());
                   // dataRepository.save(chrolingData);
                } catch (Exception e) {
                    // 오류가 발생하면 출력
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

