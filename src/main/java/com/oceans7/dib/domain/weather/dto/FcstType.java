package com.oceans7.dib.domain.weather.dto;

public enum FcstType {
    T1H("기온", "℃"),
    RN1("1시간 강수량", "mm"),
    UUU("동서바람성분", "m/s"),
    VVV("남북바람성분", "m/s"),
    REH("습도", "%"),
    PTY("강수 형태", "코드값"),
    VEC("풍향", "deg"),
    WSD("풍속", "m/s"),
    ;

    private String description;
    private String unit;

    FcstType(String description, String unit) {
        this.description = description;
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }
}
