package com.oceans7.dib.domain.location.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class LocationResponseDto {

    @Schema(description = "위도", example = "36.000000")
    private float latitude;

    @Schema(description = "경도", example = "127.000000")
    private float longitude;

    public static LocationResponseDto of(float latitude, float longitude) {
        LocationResponseDto locationResponseDto = new LocationResponseDto();
        locationResponseDto.latitude = latitude;
        locationResponseDto.longitude = longitude;
        return locationResponseDto;
    }
}
