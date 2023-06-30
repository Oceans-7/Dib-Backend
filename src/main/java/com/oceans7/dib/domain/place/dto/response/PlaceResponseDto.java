package com.oceans7.dib.domain.place.dto.response;

import com.oceans7.dib.openapi.dto.response.tourapi.list.TourAPICommonListResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaceResponseDto {
    @ArraySchema(schema = @Schema(description = "장소 정보", implementation = SimplePlaceInformationDto.class))
    private SimplePlaceInformationDto[] places;

    @Schema(description = "검색 결과 개수", example = "1")
    private int count;

    @Schema(description = "페이지 번호", example = "1")
    private int page;

    @Schema(description = "페이지 크기", example = "10")
    private int pageSize;

    public PlaceResponseDto(SimplePlaceInformationDto[] simpleDto, TourAPICommonListResponse list) {
        this.places = simpleDto;
        this.count = list.getTotalCount();
        this.page = list.getPage();
        this.pageSize = list.getPageSize();
    }
}
