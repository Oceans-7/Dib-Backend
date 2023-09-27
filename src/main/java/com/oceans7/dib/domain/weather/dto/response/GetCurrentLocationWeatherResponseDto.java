package com.oceans7.dib.domain.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class GetCurrentLocationWeatherResponseDto {

    @Schema(description = "주소", example = "XX도 XX시 XX구")
    private String address;

    @Schema(description = "날짜", example = "2021-07-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime;

    @Schema(description = "날씨 정보", implementation = WeatherInformation.class)
    private WeatherInformation weather;

}
