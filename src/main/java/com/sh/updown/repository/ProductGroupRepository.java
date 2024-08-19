package com.sh.updown.repository;

import com.sh.updown.entity.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {
    List<ProductGroup> findByCreateDate(LocalDate currentDate);
}
