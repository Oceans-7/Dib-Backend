package com.oceans7.dib.domain.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.domain.weather.service.vo.DivingIndicator;
import com.oceans7.dib.global.util.CommonUtil;
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

    @Schema(description = "풍속(m/s)", example = "5.00")
    private Double windSpeed;

    @Schema(description = "파고(m)", example = "1.00")
    private Double waveHeight;

    @Schema(description = "다이빙 지수", example = "GOOD", implementation = DivingIndicator.class)
    private DivingIndicator divingIndicator;

    @ArraySchema(schema = @Schema(description = "조수 정보", implementation = TideEvent.class))
    private List<TideEvent> tideEvents;

    public static WeatherInformation of(LocalDate localDate, WeatherType weatherType, Double airTemperature, Double waterTemperature, Double windSpeed, Double waveHeight, DivingIndicator divingIndicator, List<TideEvent> tideEvents) {
        WeatherInformation weatherInformation = new WeatherInformation();
        weatherInformation.localDate = localDate;
        weatherInformation.weatherType = weatherType;
        weatherInformation.airTemperature = CommonUtil.round(airTemperature, 1);
        weatherInformation.waterTemperature = CommonUtil.round(waterTemperature, 1);
        weatherInformation.windSpeed = CommonUtil.round(windSpeed, 1);
        weatherInformation.waveHeight = CommonUtil.round(waveHeight, 1);
        weatherInformation.divingIndicator = divingIndicator;
        weatherInformation.tideEvents = tideEvents;
        return weatherInformation;
    }
}
