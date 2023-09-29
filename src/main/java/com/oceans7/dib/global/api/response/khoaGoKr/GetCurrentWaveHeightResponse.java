package com.oceans7.dib.global.api.response.khoaGoKr;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.oceans7.dib.global.api.response.BaseAPiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetCurrentWaveHeightResponse {

    @JsonProperty("result")
    private Result result;

    @JsonCreator
    public GetCurrentWaveHeightResponse(@JsonProperty("result") Result result) {
        this.result = result;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        @JsonProperty("data")
        private List<WaveHeight> data;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WaveHeight extends BaseAPiResponse {

        @JsonProperty("record_time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime recordTime;


        @JsonProperty("wave_height")
        private String waveHeight;
    }
}

