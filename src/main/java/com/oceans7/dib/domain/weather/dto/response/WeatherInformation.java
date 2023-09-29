package com.oceans7.dib.domain.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.domain.weather.service.vo.DivingIndicator;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class WeatherInformation {

    @Schema(description = "날짜", example = "2021-07-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;

    @Schema(description = "날씨 타입", example = "SUNNY")
    private WeatherType weatherType;

    @Schema(description = "기온", example = "15.00")
    private Double airTemperature;

    @Schema(description = "수온", example = "15.00")
    private Double waterTemperature;

    @Schema(description = "풍속", example = "5.00")
    private Double windSpeed;

    @Schema(description = "파고", example = "1.00")
    private Double waveHeight;

    @Schema(description = "다이빙 지수", example = "GOOD")
    private DivingIndicator divingIndicator;

    @ArraySchema(schema = @Schema(description = "조수 정보", implementation = TideEvent.class))
    private List<TideEvent> tideEvents;
}
