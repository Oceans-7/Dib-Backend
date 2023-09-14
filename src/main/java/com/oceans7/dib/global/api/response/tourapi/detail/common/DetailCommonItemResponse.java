package com.oceans7.dib.global.api.response.tourapi.detail.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonItemResponse;
import com.oceans7.dib.global.util.TextManipulatorUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공통 정보
 */
@Getter
public class DetailCommonItemResponse extends TourAPICommonItemResponse {

    // 홈페이지 URL
    @JsonProperty("homepage")
    private String homepageUrl;

    // 개요
    @JsonProperty("overview")
    private String overview;

    public DetailCommonItemResponse(TourAPICommonItemResponse item, String homepageUrl, String overview) {
        super(item.getContentId(), item.getContentTypeId(), item.getTitle(), item.getTel(), item.getThumbnail(),
                item.getAddr1(), item.getAddr2(), item.getAddress(), item.getMapX(), item.getMapY(), item.getDistance(),
                item.getAreaCode(), item.getSigunguCode(), item.getModifiedTime());
        this.homepageUrl = homepageUrl;
        this.overview = overview;
    }

    public String extractHomepageUrl() {
        return TextManipulatorUtil.extractUrl(this.homepageUrl);
    }

    public String extractOverview() {
        return TextManipulatorUtil.replaceBrWithNewLine(this.overview);
    }
}
