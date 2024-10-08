package com.up.down.batch.indexing.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.up.down.batch.common.entity.*;
import com.up.down.batch.common.repository.ProductGroupRepository;
import com.up.down.batch.common.repository.ProductRepository;
import com.up.down.batch.indexing.entity.ProductGroupDoc;
import com.up.down.batch.indexing.repository.ProductGroupDocRepository;
import com.up.down.batch.indexing.service.ProductListJsonConvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Indexer {

    // database repo
    private final ProductRepository productRepo;
    private final ProductGroupRepository productGroupRepo;
    // elastic repo
    private final ProductGroupDocRepository productGroupDocRepo;
    // json convert service
    private final ProductListJsonConvertService jsonConvertService;

    private final LocalDate currentDate = LocalDate.now(); // DB에 저장된 데이터 중 금일 진행한 테이만 인덱싱

    public void storeInDatabase() {
        // 패키지 여행 상품 목록 조회
        List<Product> products = this.productRepo.findByCreateDate(this.currentDate);

        // 패키지 여행 상품 그룹핑
        Map<ProductGroupKey, List<Product>> productGroupByKey = products.stream()
                .collect((Collectors.groupingBy(product ->
                        new ProductGroupKey(
                                product.getProductInformation().getDestination(),
                                product.getProductInformation().getNights(),
                                product.getProductInformation().getStartDate()
                        )
                )));

        // 패키지 여행 상품 그룹 생성
        productGroupByKey.forEach((key, productList) -> {
            Destination destination = key.destination(); // 여행지
            int nights = key.nights(); // 숙박일
            LocalDate startDate = key.startDate(); // 출발일

            //
            Map<Long, ProductInformation> productInformationMap = productList.stream()
                    .collect((Collectors.toMap(Product::getId, Product::getProductInformation)));

            Set<String> keywordSet = new HashSet<>(); // 검색 키워드 생성
            keywordSet.add(destination.getKorName());
            keywordSet.addAll(getRandomThemesKeywordSet()); // 랜덤 테마 2가지 삽입

            // 패키지 여행 상품 그룹 값 대입
            ProductGroup productGroup = ProductGroup.builder()
                    .destination(destination)
                    .nights(nights)
                    .startDate(startDate)
                    .createDate(this.currentDate)
                    .searchKeywords(SearchKeyword.builder()
                            .keywordSet(keywordSet)
                            .build())
                    .productList(productInformationMap)
                    .build();

            // 패키지 여행 상품 그룹 저장
            this.productGroupRepo.save(productGroup);
        });
    }

    public void indexInElasticsearch() {
        // DB에 저장된 모든 productGroup 가져오기
        List<ProductGroup> productGroups = this.productGroupRepo.findByCreateDate(this.currentDate);

        // 기존 인덱싱 삭제
        this.productGroupDocRepo.deleteAll();

        // Elasticsearch에 인덱싱
        productGroups.forEach(productGroup -> {
            try {
                ProductGroupDoc productGroupDoc = ProductGroupDoc.builder()
                        .id(productGroup.getId())
                        .searchKeywords(productGroup.getSearchKeywords().getSearchKeyword())
                        .destination(productGroup.getDestination())
                        .nights(productGroup.getNights())
                        .startDate(productGroup.getStartDate())
                        .productListJson(jsonConvertService.convertProductListToJson(productGroup.getProductList())) // JsonProcessingException
                        .viewCount(productGroup.getViewCount())
                        .likeCount(productGroup.getLikeCount())
                        .build();

                this.productGroupDocRepo.save(productGroupDoc);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    private Set<String> getRandomThemesKeywordSet() {
        Set<String> keywordSet = new HashSet<>();
        Random random = new Random();

        TravelTheme[] allThemes = TravelTheme.values();

        while (keywordSet.size() < 2) {
            keywordSet.add(allThemes[random.nextInt(allThemes.length)].getDisplayKorName());
        }

        return keywordSet;
    }
}
