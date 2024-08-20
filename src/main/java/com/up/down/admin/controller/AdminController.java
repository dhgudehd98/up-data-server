package com.up.down.admin.controller;

import com.up.down.admin.service.AdminService;
import com.up.down.batch.indexing.entity.ProductGroupDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

    // Create or Update a document
    @PostMapping
    public ProductGroupDoc saveDoc(@RequestBody ProductGroupDoc productGroupDoc) {
        log.info("POST /elastic - Creating/Updating a document");
        return this.service.save(productGroupDoc);
    }

    // Get all documents
    @GetMapping
    public List<ProductGroupDoc> getAllDocs() {
        log.info("GET /elastic - Fetching all documents");
        return this.service.findAll();
    }

    // Delete all documents
    @DeleteMapping
    public void deleteAllDocs() {
        log.info("DELETE /elastic - Deleting all documents");
        this.service.deleteAll();
    }
}
