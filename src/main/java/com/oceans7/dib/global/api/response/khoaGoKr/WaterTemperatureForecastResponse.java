package com.oceans7.dib.global.api.response.khoaGoKr;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.global.api.response.BaseAPiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Getter
public class WaterTemperatureForecastResponse extends BaseAPiResponse {

    @JsonProperty("result")
    private WaterTemperatureForecastResponse.Result result;

    @JsonCreator
    public WaterTemperatureForecastResponse(@JsonProperty("result") WaterTemperatureForecastResponse.Result result) {
        this.result = result;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        @JsonProperty("data")
        private List<WaterTemperatureData> data;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WaterTemperatureData {
        @JsonProperty("hour")
        private String hour;

        @JsonProperty("date")
        private String date;

        @JsonProperty("temperature")
        private String temperature;
    }
}
