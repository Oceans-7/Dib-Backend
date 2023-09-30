package com.oceans7.dib.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTimeUtil {

    public final static int NCST_CALLABLE_TIME = 40;
    public final static int FCST_CALLABLE_TIME = 60;

    public static String calculateBaseDate(LocalDateTime now, int callableTime) {
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 자정
        if (now.getHour() < 2 && now.getMinute() < callableTime) {
            baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        return baseDate;
    }

    //TODO 이거 locationService에서 그대로 가져온 건데, 1시간 전 시간을 못가져오는 경우가 있어서 2시간 전 시간을 가져오도록 수정해서 사용
    //ex) 16:11에 basetime 1500 조회 불가한 경우 발생
    public static String calculateBaseTime(LocalDateTime now, int callableTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH00");
        return now.getMinute() < callableTime ?
                now.minusHours(2).format(timeFormatter) : now.format(timeFormatter);
    }
}
