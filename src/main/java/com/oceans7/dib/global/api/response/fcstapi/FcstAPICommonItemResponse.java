package com.oceans7.dib.global.api.response.fcstapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FcstAPICommonItemResponse {
    @JsonProperty("baseDate")
    private String baseDate;

    @JsonProperty("baseTime")
    private String baseTime;

    @JsonProperty("obsrValue")
    private String obsrValue;

    @JsonProperty("category")
    private String category;

    // 초단기 예보값
    @JsonProperty("fcstDate")
    private String fcstDate;

    @JsonProperty("fcstTime")
    private String fcstTime;

    @JsonProperty("fcstValue")
    private String fcstValue;
}
