package com.oceans7.dib.domain.weather.service.vo;

public enum DivingIndicator {
    VERY_GOOD("매우좋음"),
    GOOD("좋음"),
    NORMAL("보통"),
    BAD("나쁨"),
    VERY_BAD("매우나쁨");

    private String value;

    DivingIndicator(String value) {
        this.value = value;
    }

    public static DivingIndicator of(String value) {
        for (DivingIndicator divingIndicator : DivingIndicator.values()) {
            if (divingIndicator.value.equals(value)) {
                return divingIndicator;
            }
        }
        return null;
    }
}
