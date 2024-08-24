package com.up.down.batch.indexing.repository;

import com.up.down.batch.indexing.entity.ProductGroupDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductGroupDocRepository extends ElasticsearchRepository<ProductGroupDoc, String> {
}
