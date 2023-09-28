package com.oceans7.dib.domain.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
public class GetCurrentWeatherResponseDto {

    @Schema(description = "주소", example = "XX도 XX시 XX구")
    private String address;

    @Schema(description = "날씨 정보", implementation = WeatherInformation.class)
    private WeatherInformation weather;

}
