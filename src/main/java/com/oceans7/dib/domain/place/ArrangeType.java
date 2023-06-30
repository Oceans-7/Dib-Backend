package com.oceans7.dib.domain.place;

public enum ArrangeType {
    TITLE("제목순", "A"),
    DATE("최신순", "C"),
    DISTANCE("거리순", "E")
    ;

    private String description;
    private String code;

    ArrangeType(String description, String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
