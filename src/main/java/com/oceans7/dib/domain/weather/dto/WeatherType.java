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
        skyMap.put(2, CLOUDY);
        skyMap.put(4, OVERCAST);
    }

    // 하늘 코드와 강수 코드를 입력받아 날씨 타입을 반환하는 함수
    public static WeatherType getWeatherType(int sky, int precipitation, boolean isThunder, boolean isDay) {
        if (isThunder) return THUNDER;

        if (precipitationMap.containsKey(precipitation)) {
            return precipitationMap.get(precipitation);
        } else if (skyMap.containsKey(sky) && precipitation == 0) {
            return isDay ? skyMap.get(sky) : (sky == 1 ? NIGHT_SUNNY : NIGHT_CLOUDY);
        }

        return null;
    }
}
