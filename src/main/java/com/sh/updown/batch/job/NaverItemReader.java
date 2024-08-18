package com.sh.updown.batch.job;

import com.sh.updown.chroling.Naver;
import com.sh.updown.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NaverItemReader implements ItemReader<ProductDto> {
    private List<ProductDto>  dtoList = new ArrayList<>();
    private final Naver naver;
    int index = 0;

    public NaverItemReader(Naver naver) throws IOException {
        this.naver = naver;
        this.dtoList = initialize();
    }
    public  List<ProductDto> initialize() throws IOException {
        System.out.println("=============================");
        System.out.println("NaverChrolling을 시작합니다.");
        System.out.println("=============================");
        return naver.naverChroling();
    }

    @Override
    public ProductDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (this.index < dtoList.size()) {

            return dtoList.get(index++);
        }
        else return null;


    }



}