package com.oceans7.dib.global.api.response.khoaGoKr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class OceanIndexPredictionResponse {

    private Result result;

    public OceanIndexPredictionResponse(@JsonProperty("result") Result result) {
        this.result = result;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        @JsonProperty("data")
        private List<IndexInfo> data;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IndexInfo {

        @JsonProperty("time_type")
        private String timeType;

        @JsonProperty("lon")
        private String lon;

        @JsonProperty("water_temp")
        private String waterTemp;

        @JsonProperty("tide_time_score")
        private String tideTimeScope;

        @JsonProperty("total_score")
        private String totalScore;

        @JsonProperty("name")
        private String name;

        @JsonProperty("date")
        private String date;

        @JsonProperty("current_speed")
        private String currentSpeed;

        @JsonProperty("wave_height")
        private String waveHeight;

        @JsonProperty("lat")
        private String lat;
    }
}