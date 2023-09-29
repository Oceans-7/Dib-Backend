package com.oceans7.dib.domain.weather.dto;

import java.util.stream.Stream;

public enum TideType {
    HIGH("고조"),
    LOW("저조"),
    ;

    private String description;

    TideType(String description) {
        this.description = description;
    }

    public static TideType getTideType(String tideType) {
        return Stream.of(TideType.values())
                .filter(t -> t.description.equals(tideType))
                .findFirst()
                .orElse(null);
    }
}
