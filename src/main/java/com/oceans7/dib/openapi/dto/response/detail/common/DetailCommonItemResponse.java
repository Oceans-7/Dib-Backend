package com.oceans7.dib.openapi.dto.response.detail.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.openapi.dto.response.simple.TourAPICommonItemResponse;
import lombok.Getter;

/**
 * 공통 정보
 */
@Getter
public class DetailCommonItemResponse extends TourAPICommonItemResponse {

    // 개요
    @JsonProperty("overview")
    private String overview;
}
