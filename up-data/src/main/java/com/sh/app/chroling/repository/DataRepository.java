package com.sh.app.chroling.repository;

import com.sh.app.chroling.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<Product, Long> {
}