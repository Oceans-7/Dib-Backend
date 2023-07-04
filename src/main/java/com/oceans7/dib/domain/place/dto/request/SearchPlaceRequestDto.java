package com.oceans7.dib.domain.place.dto.request;

import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.domain.place.dto.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPlaceRequestDto {
    @Schema(description = "키워드 (필수)", example = "전라남도 해남")
    private String keyword;

    @Schema(description = "사용자 위도 (필수)", example = "126.9779692")
    private double mapX;

    @Schema(description = "사용자 경도 (필수)", example = "37.566535")
    private double mapY;

    @Schema(description = "관광지 타입 (선택)", example = "TOURIST_SPOT")
    private ContentType contentType;

    @Schema(description = "지역 (선택)", example = "서울")
    private String area;

    @Schema(description = "시군구 (선택, area 필드 필수)", example = "성북구")
    private String sigungu;

    @Schema(description = "정렬 구분 (선택)", example = "TITLE")
    private ArrangeType arrangeType;

    @Schema(description = "페이지 번호 (필수)", example = "1")
    private int page;

    @Schema(description = "페이지 사이즈 (필수)", example = "10")
    private int pageSize;
}
