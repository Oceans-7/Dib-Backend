package com.oceans7.dib.domain.place.dto;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.global.util.ValidatorUtil;
import lombok.*;

@Getter
@EqualsAndHashCode
public class PlaceFilterOptions {
    private FilterType filterType;
    private String contentType;
    private String arrangeType;
    private String area;
    private String sigungu;
    private String areaCode;
    private String sigunguCode;

    @Builder(builderMethodName = "initialBuilder", builderClassName = "withInitialBuilder")
    public PlaceFilterOptions(GetPlaceRequestDto request) {
        this.contentType = ValidatorUtil.isNotEmpty(request.getContentType()) ? String.valueOf(request.getContentType().getCode()) : "";
        this.arrangeType = ValidatorUtil.isNotEmpty(request.getArrangeType()) ? request.getArrangeType().name() : ArrangeType.E.name();
        this.area = ValidatorUtil.isNotEmpty(request.getArea()) ? request.getArea() : "";
        this.sigungu = ValidatorUtil.isNotEmpty(request.getSigungu()) ? request.getSigungu() : "";
        applyFilters();
    }

    @Builder(builderMethodName = "areaFilterBuilder", builderClassName = "withAreaFilterBuilder")
    public PlaceFilterOptions(FilterType filterType, String contentType, String arrangeType, String area, String sigungu, String areaCode, String sigunguCode) {
        this.filterType = filterType;
        this.contentType = contentType;
        this.arrangeType = arrangeType;
        this.area = area;
        this.sigungu = sigungu;
        this.areaCode = areaCode;
        this.sigunguCode = sigunguCode;
    }

    public PlaceFilterOptions withAreaCodeAndSigunguCode(String areaCode, String sigunguCode) {
        return PlaceFilterOptions.areaFilterBuilder()
                .filterType(this.filterType)
                .contentType(this.contentType)
                .arrangeType(this.arrangeType)
                .area(this.area)
                .sigungu(this.sigungu)
                .areaCode(areaCode)
                .sigunguCode(sigunguCode)
                .build();
    }

    private void applyFilters() {
        if (isEmptyArea()) {
            filterType = FilterType.LOCATION_BASED;
        } else {
            filterType = FilterType.AREA_BASED;
        }

        if (contentType.equals(String.valueOf(ContentType.DIVING.getCode()))) {
            filterType = FilterType.DIVING;
        }
    }

    public boolean isEmptyArea() {
        return ValidatorUtil.isEmpty(area);
    }

    public boolean isEmptySigungu() {
        return ValidatorUtil.isEmpty(sigungu);
    }

    public boolean isEmptyContentType() {
        return ValidatorUtil.isEmpty(contentType);
    }

    public boolean isEmptyArrangeType() {
        return ValidatorUtil.isEmpty(arrangeType);
    }
}
