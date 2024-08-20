package com.up.down.batch.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "tbl_product_group")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Destination destination; // 여행지

    private int nights; // 숙박일

    // 관리 정보
    private LocalDate createDate; // 생성일

    @Embedded
    @Column(name = "search_keyword")
    private SearchKeyword searchKeywords; // 검색 키워드 set

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_product_group_products",
            joinColumns = @JoinColumn(name = "product_group_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "product_information")
    private Map<Long, ProductInformation> productList; // 상품 목록

    private int viewCount; // 조회수
}
