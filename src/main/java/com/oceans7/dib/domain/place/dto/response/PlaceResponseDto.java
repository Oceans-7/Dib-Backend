package com.oceans7.dib.domain.place.dto.response;

import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonListResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaceResponseDto {
    @ArraySchema(schema = @Schema(description = "장소 정보", implementation = SimplePlaceInformationDto.class))
    private List<SimplePlaceInformationDto> places;

    @Schema(description = "검색 결과 개수", example = "1")
    private int count;

    @Schema(description = "페이지 번호", example = "1")
    private int page;

    @Schema(description = "페이지 크기", example = "10")
    private int pageSize;

    @Schema(description = "정렬 형식", example = "TITLE")
    private ArrangeType arrangeType;

    public static PlaceResponseDto of(List<SimplePlaceInformationDto> simpleDto, int count, int page, int pageSize, ArrangeType arrangeType) {
        PlaceResponseDto place = new PlaceResponseDto();

        place.places = simpleDto;
        place.count = count;
        place.page = page;
        place.pageSize = pageSize;
        place.arrangeType = arrangeType;

        return place;
    }
}
