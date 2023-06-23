package com.oceans7.dib.domain.place.dto.request;

import com.oceans7.dib.domain.place.ArrangeType;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.ServiceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceRequestDto {
    @Schema(description = "사용자 위도", example = "126.9779692")
    private double mapX;

    @Schema(description = "사용자 경도", example = "37.566535")
    private double mapY;

    @Schema(description = "관광지 타입", example = "TOURIST_SPOT")
    private ContentType contentType;

    @Schema(description = "서비스 분류", example = "LITERATURE_ART_TOUR")
    private ServiceType serviceType;

    @Schema(description = "지역", example = "서울")
    private String area;

    @Schema(description = "시군구", example = "성북구")
    private String sigungu;

    @Schema(description = "정렬 구분", example = "A")
    private ArrangeType arrangeType;

    @Schema(description = "페이지 번호", example = "1")
    private int page;

    @Schema(description = "페이지 사이즈", example = "10")
    private int pageSize;
}
