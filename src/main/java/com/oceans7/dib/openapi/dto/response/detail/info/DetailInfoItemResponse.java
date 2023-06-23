package com.oceans7.dib.openapi.dto.response.detail.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 반복 정보
 */
@Getter
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
