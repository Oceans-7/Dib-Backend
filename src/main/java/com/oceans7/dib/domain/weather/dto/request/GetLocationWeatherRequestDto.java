package com.oceans7.dib.domain.weather.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetLocationWeatherRequestDto {

    @NotNull
    @Schema(description = "위도", example = "36.146175")
    private double latitude;

    @NotNull
    @Schema(description = "경도", example = "129.269271")
    private double longitude;

}
