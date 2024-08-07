package com.sh.app.chroling.entity;




import lombok.*;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tbl_product")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "source_site")
    private String sourceSite; // 출처 페이지
    @Embedded
    private ProductInformation productInformation;

    private int viewCount;  // 조회수
    @ElementCollection(targetClass = TravelCategory.class)
    @CollectionTable(
            name = "tbl_travel_category",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<TravelCategory> travelCategories; // 여행 카테고리
    private boolean isVisible; // 페이지 노출 가능 여부
}