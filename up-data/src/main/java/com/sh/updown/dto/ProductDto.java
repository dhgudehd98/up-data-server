package com.sh.updown.dto;



import com.sh.updown.entity.ProductInformation;
import com.sh.updown.entity.TravelCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private String sourceSite; // 출처 페이지
    private ProductInformation productInformationDto;
    private int viewCount;  // 조회수
    private Set<TravelCategory> travelCategories;
    private boolean isVisible; // 페이지 노출 가능 여부



}