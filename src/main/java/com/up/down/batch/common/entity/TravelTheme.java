package com.up.down.batch.common.entity;

import lombok.Getter;

@Getter
public enum TravelTheme {
    ADVENTURE("Adventure", "모험"),
    RELAXATION("Relaxation", "휴식"),
    CULTURE("Culture", "문화"),
    NATURE("Nature", "자연"),
    FOOD("Food", "맛집"),
    HISTORY("History", "역사"),
    LUXURY("Luxury", "럭셔리"),
    FAMILY("Family", "가족"),
    ROMANTIC("Romantic", "로맨틱"),
    SHOPPING("Shopping", "쇼핑"),
    FESTIVAL("Festival", "축제");

    private final String displayName;
    private final String displayKorName;

    TravelTheme(String displayName, String displayKorName) {
        this.displayName = displayName;
        this.displayKorName = displayKorName;
    }
}
