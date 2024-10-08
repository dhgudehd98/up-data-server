package com.up.down.batch.crawling.job;

import com.up.down.batch.common.dto.ProductDto;
import com.up.down.batch.common.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.ItemProcessor;


@Slf4j
@RequiredArgsConstructor
public class NaverItemProcessor implements ItemProcessor<ProductDto, Product> {

    Product product = new Product();


    @Override
    public Product process(ProductDto productDto) throws Exception {
        Product product = this.product.toEntity(productDto);
        return product;
    }
}