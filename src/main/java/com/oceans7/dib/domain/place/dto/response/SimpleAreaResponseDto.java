package com.oceans7.dib.domain.place.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleAreaResponseDto {

    @Schema(description = "주소", example = "경기도 여주시")
    private String address;

    @Schema(description = "거리 (km)", example = "21.0")
    private double distance;

    @Schema(description = "depth 1 지역명", example = "경기도")
    private String areaName;

    @Schema(description = "depth 2 시군구명", example = "여주시")
    private String sigunguName;

    @Schema(description = "장소 위도", example = "126.9779692")
    private double mapX;

    @Schema(description = "장소 경도", example = "37.566535")
    private double mapY;

    public static SimpleAreaResponseDto of(String address, double distance, String areaName, String sigunguName,
                                           double mapX, double mapY) {
        SimpleAreaResponseDto simpleAreaResponseDto = new SimpleAreaResponseDto();

        simpleAreaResponseDto.address = address;
        simpleAreaResponseDto.distance = distance;
        simpleAreaResponseDto.areaName = areaName;
        simpleAreaResponseDto.sigunguName = sigunguName;
        simpleAreaResponseDto.mapX = mapX;
        simpleAreaResponseDto.mapY = mapY;

        return simpleAreaResponseDto;
    }

}
