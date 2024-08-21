package com.up.down.batch.indexing.entity;

import com.up.down.batch.common.entity.Destination;
import com.up.down.batch.common.entity.ProductGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Document(indexName = "product_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductGroupDoc {
    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    private String searchKeywords; // 검색 키워드

    @Field(type = FieldType.Keyword)
    private Destination destination; // 여행지

    @Field(type = FieldType.Integer)
    private int nights; // 숙박일

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd")
    private LocalDate startDate; // 여행 시작일

    @Field(type = FieldType.Text)  // JSON 문자열을 저장하기 위한 필드
    private String productListJson; // 상품목록

    @Field(type = FieldType.Integer)
    private int viewCount; // 조회수

    @Field(type = FieldType.Integer)
    private int likeCount; // 좋아요
}