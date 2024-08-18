package com.sh.updown.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "tbl_product_group")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProductGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Destination destination; // 여행지
    private int nights; // 숙박일

    // 관리 정보
    private LocalDate createDate; // 생성일
    @ElementCollection(
            fetch = FetchType.EAGER,
            targetClass = ProductInformation.class
    )
    @CollectionTable(
            name = "tbl_product_group_products",
            joinColumns = @JoinColumn(name = "product_group_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "product_information")
    private Map<Long, ProductInformation> productList; // 상품 목록
    private int viewCount; // 조회수

    public void increaseViewCount() {
        this.viewCount++;
    }
}
