package com.oceans7.dib.global.api.response.khoaGoKr;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.global.api.response.kakaoAuth.OpenKeyListResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class TidePredictionListResponse {

    @JsonProperty("result")
    private Result result;

    @JsonCreator
    public TidePredictionListResponse(@JsonProperty("result") Result result) {
        this.result = result;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        @JsonProperty("data")
        private List<TideData> data;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TideData {
        @JsonProperty("hl_code")
        private String hlCode;

        @JsonProperty("tph_time")
        private String tphTime;

        @JsonProperty("tph_level")
        private String tphLevel;
    }
}
