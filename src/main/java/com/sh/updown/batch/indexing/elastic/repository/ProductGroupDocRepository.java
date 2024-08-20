package com.sh.updown.batch.indexing.elastic.repository;

import com.sh.updown.batch.indexing.elastic.entity.ProductGroupDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductGroupDocRepository extends ElasticsearchRepository<ProductGroupDoc, String> {
}
