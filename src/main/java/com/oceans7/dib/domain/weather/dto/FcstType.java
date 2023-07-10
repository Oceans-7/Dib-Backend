package com.oceans7.dib.domain.weather.dto;

public enum FcstType {
    T1H("기온"),
    RN1("1시간 강수량"),
    SKY("하늘상태"),
    UUU("동서바람성분"),
    VVV("남북바람성분"),
    REH("습도"),
    PTY("강수 형태"),
    VEC("풍향"),
    WSD("풍속"),
    LGT("낙뢰")
    ;

    private String description;

    FcstType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
