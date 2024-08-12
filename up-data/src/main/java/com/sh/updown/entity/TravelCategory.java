package com.sh.updown.entity;

import lombok.Getter;

@Getter
public enum TravelCategory {
    HISTORICAL("역사"),
    NATURE("자연"),
    GASTRONOMY("미식"),
    CULTURAL("문화"),
    RELAXATION("휴양"),
    ADVENTURE("모험"),
    CITY_TOUR("도시"),
    ART("예술"),
    FESTIVAL("축제"),
    SPORTS("스포츠");

    private final String koreanName;

    TravelCategory(String koreanName) {
        this.koreanName = koreanName;
    }
}