package com.oceans7.dib.openapi.dto.response.tourapi.detail.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.openapi.dto.response.tourapi.list.TourAPICommonItemResponse;
import lombok.Getter;

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
}
