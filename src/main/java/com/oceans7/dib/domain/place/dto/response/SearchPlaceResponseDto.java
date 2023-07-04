package com.oceans7.dib.domain.place.dto.response;

import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonListResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchPlaceResponseDto {

    @Schema(description = "검색어", example = "전라남도 해남")
    private String keyword;

    @ArraySchema(schema = @Schema(description = "장소 정보", implementation = SimplePlaceInformationDto.class))
    private SimplePlaceInformationDto[] places;

    @Schema(description = "검색 결과 개수", example = "1")
    private int count;

    @Schema(description = "페이지 번호", example = "1")
    private int page;

    @Schema(description = "페이지 크기", example = "10")
    private int pageSize;

    @Schema(description = "정렬 형식", example = "TITLE")
    private ArrangeType arrangeType;

    public static SearchPlaceResponseDto of(String keyword, SimplePlaceInformationDto[] simpleDto, TourAPICommonListResponse list,
                                            ArrangeType arrangeType) {
        SearchPlaceResponseDto searchPlaceResponse = new SearchPlaceResponseDto();

        searchPlaceResponse.keyword = keyword;
        searchPlaceResponse.places = simpleDto;
        searchPlaceResponse.count = list.getTotalCount();
        searchPlaceResponse.page = list.getPage();
        searchPlaceResponse.pageSize = list.getPageSize();
        searchPlaceResponse.arrangeType = arrangeType;

        return searchPlaceResponse;
    }

}
