package com.oceans7.dib.domain.weather.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class GetForeCastWeatherResponseDto {

    @Schema(description = "주소", example = "XX도 XX시 XX구 XX동")
    private String address;

    @ArraySchema(schema = @Schema(description = "날씨 정보", implementation = WeatherInformation.class))
    private List<WeatherInformation> weathers;

}

