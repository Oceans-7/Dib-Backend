package com.oceans7.dib.domain.place.dto.response;

import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonListResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchPlaceResponseDto {

    @Schema(description = "검색어", example = "전라남도 해남")
    private String keyword;

    @ArraySchema(schema = @Schema(description = "장소 정보", implementation = SimplePlaceInformationDto.class))
    private List<SimplePlaceInformationDto> places;

    @ArraySchema(schema = @Schema(description = "지역 정보 (지역명 검색시)", implementation = SimpleAreaResponseDto.class))
    private List<SimpleAreaResponseDto> areas;

    @Schema(description = "검색 결과 개수", example = "1")
    private int count;

    @Schema(description = "페이지 번호", example = "1")
    private int page;

    @Schema(description = "페이지 크기", example = "10")
    private int pageSize;

    @Schema(description = "지역명 검색 시 true: 지역명 검색 시 areas 변수로 결과 전달", example = "true")
    private boolean isAreaSearch;

    public static SearchPlaceResponseDto of(String keyword, List<SimplePlaceInformationDto> simpleDto, boolean isAreaSearch,
                                            int count, int page, int pageSize) {
        SearchPlaceResponseDto searchPlaceResponse = new SearchPlaceResponseDto();

        searchPlaceResponse.keyword = keyword;
        searchPlaceResponse.places = simpleDto;
        searchPlaceResponse.count = count;
        searchPlaceResponse.page = page;
        searchPlaceResponse.pageSize = pageSize;
        searchPlaceResponse.isAreaSearch = isAreaSearch;

        return searchPlaceResponse;
    }

    public static SearchPlaceResponseDto of(String keyword, List<SimpleAreaResponseDto> simpleDto, boolean isAreaSearch) {
        SearchPlaceResponseDto searchPlaceResponse = new SearchPlaceResponseDto();

        searchPlaceResponse.keyword = keyword;
        searchPlaceResponse.areas = simpleDto;
        searchPlaceResponse.isAreaSearch = isAreaSearch;

        return searchPlaceResponse;
    }

}
