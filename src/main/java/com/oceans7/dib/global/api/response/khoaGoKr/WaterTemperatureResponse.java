package com.oceans7.dib.global.api.response.khoaGoKr;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Getter
public class WaterTemperatureResponse {

    @JsonProperty("result")
    private Result result;

    @JsonCreator
    public WaterTemperatureResponse(@JsonProperty("result") Result result) {
        this.result = result;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        @JsonProperty("data")
        private LinkedList<WaterTemperatureData> data;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WaterTemperatureData {
        @JsonProperty("record_time")
        private String recordTime;

        @JsonProperty("water_temp")
        private String waterTemp;
    }
}
