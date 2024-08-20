package com.up.down.admin.service;

import com.up.down.batch.indexing.entity.ProductGroupDoc;
import com.up.down.batch.indexing.repository.ProductGroupDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final ProductGroupDocRepository repo;

    // Create or Update
    public ProductGroupDoc save(ProductGroupDoc productGroupDoc) {
        return this.repo.save(productGroupDoc);
    }

    // Read all documents
    public List<ProductGroupDoc> findAll() {
        Iterable<ProductGroupDoc> iterable = this.repo.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    // Delete all documents
    public void deleteAll() {
        this.repo.deleteAll();
    }
}
