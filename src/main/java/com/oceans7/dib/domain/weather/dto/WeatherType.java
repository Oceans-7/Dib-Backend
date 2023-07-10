package com.oceans7.dib.domain.weather.dto;

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

    // 하늘 코드와 강수 코드를 입력받아 날씨 타입을 반환하는 함수
    public static WeatherType getWeatherType(int sky, int precipitation, boolean isThunder, boolean isDay) {
        if(isThunder) return THUNDER;

        switch(precipitation) {
            case 1:
            case 5:
                return RAINY;
            case 2:
            case 3:
            case 6:
            case 7:
                return SNOWY;
            case 0: {
                switch(sky) {
                    case 1: {
                        if(isDay) return SUNNY;
                        else return NIGHT_SUNNY;
                    }
                    case 2: {
                        if(isDay) return CLOUDY;
                        else return NIGHT_CLOUDY;
                    }
                    case 4:
                        return OVERCAST;
                }
            }
        }

        return null;
    }
}
