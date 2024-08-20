package com.sh.updown.batch.job;

import com.sh.updown.dto.ProductDto;
import com.sh.updown.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.ItemProcessor;


@Slf4j
@RequiredArgsConstructor
public class NaverItemProcessor implements ItemProcessor<ProductDto, ProductEntity> {

    ProductEntity productEntity = new ProductEntity();


    @Override
    public ProductEntity process(ProductDto productDto) throws Exception {
        ProductEntity product = productEntity.toEntity(productDto);
        return product;
    }
}