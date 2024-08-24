package com.up.down.batch.indexing.job;

import com.up.down.batch.common.entity.Destination;

import java.time.LocalDate;

/**
 * ProductGroup을 그룹핑하기 위한 클래스
 * @param destination 여행지
 * @param nights 숙박일
 * @param startDate 여행 출발일
 */
public record ProductGroupKey(Destination destination, int nights, LocalDate startDate) {
}
