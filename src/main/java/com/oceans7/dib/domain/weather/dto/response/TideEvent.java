package com.oceans7.dib.domain.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oceans7.dib.domain.weather.dto.TideType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TideEvent {

    @Schema(description = "시간", example = "2021-07-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @Schema(description = "높이", example = "1.00")
    private double height;

    @Schema(description = "조수 타입(만조, 간조)", example = "HIGH")
    private TideType type;

    public static TideEvent of(LocalDateTime time, double height, String type) {
        TideEvent tideEvent = new TideEvent();
        tideEvent.time = time;
        tideEvent.height = height;
        tideEvent.type = TideType.getTidType(type);
        return tideEvent;
    }

}
