package com.oceans7.dib.domain.weather.dto.response;

import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.domain.weather.service.vo.DivingIndicator;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WeatherInformation {

    @Schema(description = "날씨 타입", example = "SUNNY")
    private WeatherType weatherType;

    @Schema(description = "날씨 타입", example = "SUNNY")
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

    public static WeatherInformation of(WeatherType weatherType, Double airTemperature, Double waterTemperature, Double windSpeed, Double waveHeight, DivingIndicator divingIndicator, List<TideEvent> tideEvents) {
        return new WeatherInformation(weatherType, airTemperature, waterTemperature, windSpeed, waveHeight, divingIndicator, tideEvents);
    }

}
