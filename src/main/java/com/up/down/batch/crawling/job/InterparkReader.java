package com.up.down.batch.crawling.job;

import com.up.down.batch.crawling.Interpark;
import com.up.down.batch.common.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class InterparkReader implements ItemReader<ProductDto> {
    int index = 0;

    // 크롤링한 데이터를 저장하는 배열
    private List<ProductDto> interparkList = new ArrayList<>();
    private final Interpark interpark;

    public InterparkReader(Interpark interpark) throws Exception {
        this.interpark = interpark;
        this.interparkList = initialize();
    }

    public List<ProductDto> initialize() throws Exception {
        return interpark.interparkChroling();
    }


    @Override
    public ProductDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (this.index < interparkList.size()) {

            return interparkList.get(index++);
        }
        return null;
    }
}