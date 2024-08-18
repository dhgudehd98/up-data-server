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
        log.debug("Process 작업을 시작합니다.");
        ProductEntity product = productEntity.toEntity(productDto);
        return product;
    }
}