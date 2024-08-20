package com.up.down.batch.common.entity;

import lombok.Getter;

@Getter
public enum Destination {
    JEJU("제주도"),
    GANGWON("강원"),
    JEOLLA("전라"),
    BUSAN("부산"),
    GEOJE("거제"),
    NAMHAE("남해"),
    TONGYEONG("통영"),
    GYEONGJU("경주"),
    YEOSU("여수"),
    ULLEUNGDO("울릉도");

    private final String korName;

    Destination(String korName) {
        this.korName = korName;
    }

    // 한글 이름을 통해 열거형 값 찾기
    public static Destination fromKrName(String krName) {
        for (Destination destination : Destination.values()) {
            if (destination.getKorName().equals(krName)) {
                return destination;
            }
        }
        throw new IllegalArgumentException("No enum constant with krName " + krName);
    }
}
