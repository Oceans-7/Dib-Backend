package com.oceans7.dib.domain.place.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchPlaceRequestDto {
    @Schema(description = "키워드", example = "전라남도 해남")
    private String keyword;

    @Schema(description = "사용자 위도", example = "126.9779692")
    private double mapX;

    @Schema(description = "사용자 경도", example = "37.566535")
    private double mapY;

    @Schema(description = "페이지 번호", example = "1")
    private int page;

    @Schema(description = "페이지 사이즈", example = "10")
    private int pageSize;
}
