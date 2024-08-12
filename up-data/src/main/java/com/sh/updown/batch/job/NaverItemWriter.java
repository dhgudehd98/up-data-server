package com.sh.updown.batch.job;

import com.sh.updown.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class NaverItemWriter implements ItemWriter<ProductEntity> {

    @Override
    public void write(List<? extends ProductEntity> list) throws Exception {

        for (ProductEntity product : list) {
            log.debug("Writer 작업을 시작합니다.");
            System.out.println(product.toString());
        }

    }
}