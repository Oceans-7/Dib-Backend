package com.oceans7.dib.domain.weather.dto;

import java.util.HashMap;
import java.util.Map;

public enum WeatherType {
    SUNNY("맑음 "),
    CLOUDY("구름 많음"),
    OVERCAST("흐림"),
    SNOWY("눈"),
    RAINY("비"),
    THUNDER("낙뢰"),
    NIGHT_SUNNY("밤 맑음"),
    NIGHT_CLOUDY("밤 구름 많음"),
    ;

    WeatherType(String description) {}

    private static final Map<Integer, WeatherType> precipitationMap = new HashMap<>();
    private static final Map<Integer, WeatherType> skyMap = new HashMap<>();

    static {
        precipitationMap.put(1, RAINY);
        precipitationMap.put(5, RAINY);

        precipitationMap.put(2, SNOWY);
        precipitationMap.put(3, SNOWY);
        precipitationMap.put(6, SNOWY);
        precipitationMap.put(7, SNOWY);

        skyMap.put(1, SUNNY);
        skyMap.put(3, CLOUDY);
        skyMap.put(4, OVERCAST);
    }

    /**
     * param 하늘 코드, 강수 코드, 낙뢰 여부, 낮밤 여부
     * return 날씨 타입
     */
    public static WeatherType getWeatherType(int sky, int precipitation, boolean isThunder, boolean isDay) {
        // case : 번개
        if (isThunder) return THUNDER;

        // case : 비/눈
        if (precipitationMap.containsKey(precipitation)) { return precipitationMap.get(precipitation);}

        // case : 밤/낮의 맑음/흐림
        if (isDay || sky == 4) { return skyMap.get(sky); }
        else { return sky == 1 ? NIGHT_SUNNY : NIGHT_CLOUDY; }
    }
}
