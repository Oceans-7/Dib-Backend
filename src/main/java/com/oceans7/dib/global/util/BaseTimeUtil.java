package com.oceans7.dib.global.util;

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
}
