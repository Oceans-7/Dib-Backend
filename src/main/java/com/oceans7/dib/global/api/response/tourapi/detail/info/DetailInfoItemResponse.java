package com.oceans7.dib.global.api.response.tourapi.detail.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 반복 정보
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailInfoItemResponse {
    @JsonProperty("contentid")
    private Long contentId;

    @JsonProperty("contenttypeid")
    private int contentTypeId;

    @JsonProperty("infoname")
    private String infoName;

    @JsonProperty("infotext")
    private String infoText;
}
