package com.oceans7.dib.global.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTimeUtil {

    public final static int NCST_CALLABLE_TIME = 40;
    public final static int FCST_CALLABLE_TIME = 60;

    public static String calculateBaseDate(LocalDateTime now, int callableTime) {
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 자정
        if (now.getHour() == 0 && now.getMinute() < callableTime) {
            baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        return baseDate;
    }

    public static String calculateBaseTime(LocalDateTime now, int callableTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH00");
        return now.getMinute() < callableTime ?
                now.minusHours(1).format(timeFormatter) : now.format(timeFormatter);
    }

    public static ForecastBaseDateTime getForecastBaseDateTime(LocalDateTime now) {
        // 매 3시간 간격으로 발표
        int forecastDurationHour = 3;
        int currentHour = now.getHour();
        int currentMinute = now.getMinute();
        // 02시, 05시, 08시, 11시, 14시, 17시, 20시, 23시 기준 데이터 조회
        int hourFromLastForecast = (currentHour + 1) % forecastDurationHour;
        // 3시간 간격의 10분에 발표
        int forecastAnnounceMinute = 10;
        int minusHour = hourFromLastForecast == 0 && currentMinute <= forecastAnnounceMinute ? forecastDurationHour : hourFromLastForecast;
        String forecastBaseDate = now.minusHours(minusHour).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String forecastBaseTime = now.minusHours(minusHour).format(DateTimeFormatter.ofPattern("HH00"));

        return ForecastBaseDateTime.of(forecastBaseDate, forecastBaseTime);
    }

    @Getter
    @AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    public static class ForecastBaseDateTime{
        private String forecastBaseDate;

        private String forecastBaseTime;
    }
}
