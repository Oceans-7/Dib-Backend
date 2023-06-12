package com.oceans7.dib.place.dto.request;

import com.oceans7.dib.place.ContentType;
import com.oceans7.dib.place.ServiceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPlaceRequestDto {

    @Schema(description = "키워드 (아마 지명으로 입력 받고 카카오 api로 위도 경도를 검색해서 위치 기반 검색 tour api로 정보 불러와야할듯", example = "전라남도 해남")
    private String keyword;

    @Schema(description = "관광지 타입", example = "TOURIST_SPOT")
    private ContentType contentType;

    @Schema(description = "서비스 분류", example = "LITERATURE_ART_TOUR")
    private ServiceType serviceType;

    @Schema(description = "페이지 번호", example = "1")
    private int page;

    @Schema(description = "페이지 사이즈", example = "10")
    private int pageSize;
}
