package com.oceans7.dib.global.api.response.tourapi.detail.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailImageItemResponse {
    // 콘텐츠 ID
    @JsonProperty("contentid")
    private Long contentId;

    // 원본 이미지
    @JsonProperty("originimgurl")
    private String originImageUrl;
}
