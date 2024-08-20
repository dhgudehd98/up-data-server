package com.sh.updown.batch.common.entity;

import com.sh.updown.batch.common.dto.ProductDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

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
    @Embedded
    private ProductInformation productInformation; // 상품 정보

    // 관리 정보
    @CreationTimestamp
    private LocalDate createDate; // 생성일
    @Column(name = "source_site", length = 500)
    private String sourceSite; // 출처 페이지
    @Column(name = "view_count")
    private int viewCount;  // 조회수

    public Product toEntity(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .sourceSite(productDto.getSourceSite())
                .productInformation(productDto.getProductInformationDto())
                .viewCount(productDto.getViewCount())
                .build();
    }
}