package com.sh.updown.entity;




import lombok.*;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInformation {
    private String title; // 제목
    private LocalDate start_date; // 여행 시작일
    private String nights; // 여행일
    private int price; // 가격
    private String area; //출발지역

    @Column(name = "thumbnail_url")
    private String thumbnailUrl; // 여행지 이미지
    @Column(name = "travel_agency")
    private String travelAgency; // 여행사
    @Column(name = "detail_url")
    private String detailUrl; // 상품상세페이지
}