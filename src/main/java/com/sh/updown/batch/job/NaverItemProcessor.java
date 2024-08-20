package com.sh.updown.batch.job;

import com.sh.updown.dto.ProductDto;
import com.sh.updown.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.ItemProcessor;


@Slf4j
@RequiredArgsConstructor
public class NaverItemProcessor implements ItemProcessor<ProductDto, Product> {

    Product product = new Product();


    @Override
    public Product process(ProductDto productDto) throws Exception {
        log.debug("Process 작업을 시작합니다.");
        Product product = this.product.toEntity(productDto);
        return product;
    }
}