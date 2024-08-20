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

    @Field(type = FieldType.Keyword)
    private Destination destination;
    @Field(type = FieldType.Integer)
    private int nights;

//    @Field(type = FieldType.Object, ignoreFields = {"*"})
//    private Map<Long, ProductInformation> productList;
    @Field(type = FieldType.Integer)
    private int viewCount;

    public ProductGroup toEntity() {
        return ProductGroup.builder()
                .id(id)
                .destination(destination)
                .nights(nights)
//                .productList(productList)
                .viewCount(viewCount)
                .build();
    }
}
