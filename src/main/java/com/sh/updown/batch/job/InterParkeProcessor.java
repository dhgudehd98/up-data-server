package com.sh.updown.batch.job;

import com.sh.updown.dto.ProductDto;
import com.sh.updown.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class InterParkeProcessor implements ItemProcessor<ProductDto, Product> {

    Product product = new Product();
    @Override
    public Product process(ProductDto productDto) throws Exception {
        Product product = this.product.toEntity(productDto);
        return product;
    }
}