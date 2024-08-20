package com.sh.updown.batch.common.dto;

import com.sh.updown.batch.common.entity.Destination;
import com.sh.updown.batch.common.entity.ProductInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private Destination destination;
    private String sourceSite; // 출처 페이지
    private ProductInformation productInformationDto;
    private int viewCount;  // 조회수
}