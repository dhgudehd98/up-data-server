package com.sh.updown.batch.indexing.elastic.entity;

import com.sh.updown.batch.common.entity.Destination;
import com.sh.updown.batch.common.entity.ProductGroup;
import com.sh.updown.batch.common.entity.ProductInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

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
    private String searchKeywords;
//    private Set<SearchKeyword> searchKeywords;

    @Field(type = FieldType.Object)
    private Destination destination;
    @Field(type = FieldType.Integer)
    private int nights;

    @Field(type = FieldType.Object)
    private Map<Long, ProductInformation> productList;
    @Field(type = FieldType.Integer)
    private int viewCount;

    public ProductGroup toEntity() {
        return ProductGroup.builder()
                .id(id)
                .destination(destination)
                .nights(nights)
                .productList(productList)
                .viewCount(viewCount)
                .build();
    }
}
