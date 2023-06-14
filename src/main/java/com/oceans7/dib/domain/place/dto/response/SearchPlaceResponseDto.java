package com.oceans7.dib.domain.place.dto.response;

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

}
