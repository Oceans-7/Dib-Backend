package com.oceans7.dib.openapi.dto.response.detail.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailImageItemResponse {
    // 콘텐츠 ID
    @JsonProperty("contentid")
    private String contentId;

    // 원본 이미지
    @JsonProperty("originimgurl")
    private String originImageUrl;
}
