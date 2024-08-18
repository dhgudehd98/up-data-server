package com.sh.updown.entity;


import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInformation {
    @Enumerated(EnumType.STRING)
    private Destination destination; // 여행지
    private String nights; // 여행일

    private String title; // 제목
    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd")
    private LocalDate start_date; // 여행 시작일
    private int price; // 가격



    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl; // 여행지 이미지
    @Column(name = "travel_agency", length = 500)
    private String travelAgency; // 여행사 이미지
    @Column(name = "detail_url", length =500)
    private String detailUrl; // 상품상세페이지
}