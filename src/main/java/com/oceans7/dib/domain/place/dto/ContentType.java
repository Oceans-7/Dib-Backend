package com.oceans7.dib.domain.place.dto;

public enum ContentType {
    TOURIST_SPOT(12),
    CULTURAL_SITE(14),
    EVENT(15),
    LEPORTS(28),
    ACCOMMODATION(32),
    SHOPPING(38),
    RESTAURANT(39),
    ;

    private int code;

    ContentType(int code) {
        this.code = code;
    }

    public int getCode() { return code; }

    public static ContentType getContentTypeByCode(int code) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.getCode() == code) {
                return contentType;
            }
        }
        return null;
    }
}
