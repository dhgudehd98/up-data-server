package com.sh.updown.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NaverList {
    List<String> naverList = new ArrayList<>();
    LocalDate now = LocalDate.now();
    LocalDate start = now.plusWeeks(1);
    LocalDate end = start.plusWeeks(1);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");

    public List<String> list() {
        //제주 , 강원, 전라, 부산, 거제, 남해, 통영, 경주, 여수, 울릉도
        for(int page = 1; page<4; page++) {
            String naverJeju = "https://pkgtour.naver.com/domestic-list?destination=14&departureDate="
                    + start.format(dateFormatter)
                    + "%2C"
                    + end.format(dateFormatter)
                    + "&page=" + page;
            String naverGangwondo = "https://pkgtour.naver.com/domestic-list?destination=01&departureDate="
                    + start.format(dateFormatter)
                    + "%2C"
                    + end.format(dateFormatter)
                    + "&page=" + page;
            String naverJeollanamdo = "https://pkgtour.naver.com/domestic-list?destination=12&departureDate="
                    + start.format(dateFormatter)
                    + "%2C"
                    + end.format(dateFormatter)
                    + "&page=" + page;
            String naverBusan = "https://pkgtour.naver.com/domestic-list?destination=08&departureDate="
                    + start.format(dateFormatter)
                    + "%2C"
                    + end.format(dateFormatter)
                    + "&page=" + page;
            String naverGeoje = "https://pkgtour.naver.com/domestic-list?destination=0331&departureDate="
                    + start.format(dateFormatter)
                    + "%2C"
                    + end.format(dateFormatter)
                    + "&page=" + page;
            String naverNamhae = "https://pkgtour.naver.com/domestic-list?destination=0384&departureDate="
                    + start.format(dateFormatter)
                    + "%2C"
                    + end.format(dateFormatter)
                    + "&page=" + page;
            String naverTongyeong = "https://pkgtour.naver.com/domestic-list?destination=0322&departureDate="
                    + start.format(dateFormatter)
                    + "%2C"
                    + end.format(dateFormatter)
                    + "&page=" + page;
            String naverGyeongju = "https://pkgtour.naver.com/domestic-list?destination=0413&departureDate="
                    + start.format(dateFormatter)
                    + "%2C"
                    + end.format(dateFormatter)
                    + "&page=" + page;
            String naverYeosu = "https://pkgtour.naver.com/domestic-list?destination=1213&departureDate="
                    + start.format(dateFormatter)
                    + "%2C"
                    + end.format(dateFormatter)
                    + "&page=" + page;
            String naverUlleungdo = "https://pkgtour.naver.com/domestic-list?destination=11491925&departureDate="
                    + start.format(dateFormatter)
                    + "%2C"
                    + end.format(dateFormatter)
                    + "&page=" + page;

            naverList.add(naverJeju);
            naverList.add(naverUlleungdo);
            naverList.add(naverGangwondo);
            naverList.add(naverJeollanamdo);
            naverList.add(naverBusan);
            naverList.add(naverGeoje);
            naverList.add(naverNamhae);
            naverList.add(naverTongyeong);
            naverList.add(naverGyeongju);
            naverList.add(naverYeosu);

        }
        return naverList;
    }

}