package com.sh.updown.entity;


import com.sh.updown.dto.ProductDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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
    private ProductInformation productInformation;

    @Column(name = "source_site" ,length = 500)
    private String sourceSite; // 출처 페이지
    @CreationTimestamp
    private LocalDate createDate;
    @Column(name = "is_visible")
    private boolean isVisible; // 페이지 노출 가능 여부
    @Column(name = "view_count")
    private int viewCount;  // 조회수

    public Product toEntity(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .sourceSite(productDto.getSourceSite())
                .productInformation(productDto.getProductInformationDto())
                .viewCount(productDto.getViewCount())
                .isVisible(productDto.isVisible())
                .build();
    }
}