package com.oceans7.dib.domain.place;

public enum ArrangeType {
    A("제목순"),
    C("최신순"),
    E("거리순")
    ;

    private String description;

    ArrangeType(String description) {
        this.description = description;
    }
}
