package com.sh.updown.elastic.repository;

import com.sh.updown.elastic.entity.ProductGroupDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticRepository extends ElasticsearchRepository<ProductGroupDoc, String> {
}
