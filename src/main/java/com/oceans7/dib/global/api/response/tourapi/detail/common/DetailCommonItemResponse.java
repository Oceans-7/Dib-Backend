package com.oceans7.dib.global.api.response.tourapi.detail.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonItemResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공통 정보
 */
@Getter
@NoArgsConstructor
public class DetailCommonItemResponse extends TourAPICommonItemResponse {

    // 홈페이지 URL
    @JsonProperty("homepage")
    private String homepageUrl;

    // 개요
    @JsonProperty("overview")
    private String overview;

    public DetailCommonItemResponse(TourAPICommonItemResponse item, String homepageUrl, String overview) {
        super(item.getContentId(), item.getContentTypeId(), item.getTitle(), item.getTel(), item.getThumbnail(),
                item.getAddr1(), item.getAddr2(), item.getAddress(), item.getMapX(), item.getMapY(), item.getDistance(),  item.getAreaCode(), item.getSigunguCode());
        this.homepageUrl = homepageUrl;
        this.overview = overview;
    }
}
