package com.oceans7.dib.domain.location.dto.response;

import com.oceans7.dib.domain.weather.dto.WeatherType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class LocationResponseDto {

    @Schema(description = "도로명 주소", example = "경기도 여주시 세종로 1")
    private String address;

    @Schema(description = "날씨 상태", example = "SUNNY")
    private WeatherType weatherType;

    @Schema(description = "기온", example = "23")
    private double temperatures;

    public static LocationResponseDto of(String address, WeatherType weatherType, double temperatures) {
        LocationResponseDto locationResponseDto = new LocationResponseDto();
        locationResponseDto.address = address;
        locationResponseDto.weatherType = weatherType;
        locationResponseDto.temperatures = temperatures;
        return locationResponseDto;
    }
}
