package com.sh.updown.batch.job;

import com.sh.updown.chroling.Interpark;
import com.sh.updown.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class InterParkReader implements ItemReader<ProductDto> {
    int index = 0;
    private List<ProductDto> interparkList = new ArrayList<>();

    private final Interpark interpark;

    public InterParkReader(Interpark interpark) throws Exception {
        this.interpark = interpark;
        this.interparkList = initialize();
    }

    public List<ProductDto> initialize() throws Exception {
        return interpark.interparkChroling();
    }

    @Override
    public ProductDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        log.debug("Interpark Chrolling을 시작합니다.");
        if (this.index < interparkList.size()) {
            log.debug("{}", interparkList.get(index));
            return interparkList.get(index++);
        }
        return null;
    }
}