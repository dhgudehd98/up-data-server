package com.up.and.down.product.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProductInformation {
    private String title; // 제목
    private LocalDateTime start_date; // 여행 시작일
    private int nights; // 여행일
    private int price; // 가격
    private String thumbnailUrl; // 여행지 이미지
    private String travelAgency; // 여행사
    private String detailUrl; // 상품상세페이지
}