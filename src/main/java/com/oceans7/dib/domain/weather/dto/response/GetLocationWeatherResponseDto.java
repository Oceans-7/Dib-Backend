package com.oceans7.dib.domain.weather.dto.response;

import com.oceans7.dib.domain.weather.TideType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetLocationWeatherResponseDto {

    @Schema(description = "주소", example = "XX도 XX시 XX구 XX동")
    private String address;

    @ArraySchema(schema = @Schema(description = "날씨 정보", implementation = WeatherInformation.class))
    private WeatherInformation[] weathers;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WeatherInformation {

        @Schema(description = "날짜", example = "2021-07-01")
        private LocalDate date;

        @Schema(description = "날씨 이미지 URL", example = "https://www.weather.go.kr/weather/images/analysischart/icon_ws01.gif")
        private String weatherImageUrl;

        @Schema(description = "기온", example = "30.00")
        private float temperature;

        @Schema(description = "수온", example = "15.00")
        private float waterTemperature;

        @Schema(description = "풍속", example = "5.00")
        private float windSpeed;

        @Schema(description = "자외선 지수", example = "5")
        private int UVIndex;

        @ArraySchema(schema = @Schema(description = "조수 정보", implementation = TideEvent.class))
        private TideEvent[] tideEvents;

        public static WeatherInformation of(LocalDate date, String weatherImageUrl, float temperature, float waterTemperature, float windSpeed, int UVIndex, TideEvent[] tideEvents) {
            WeatherInformation weatherInformation = new WeatherInformation();
            weatherInformation.date = date;
            weatherInformation.weatherImageUrl = weatherImageUrl;
            weatherInformation.temperature = temperature;
            weatherInformation.waterTemperature = waterTemperature;
            weatherInformation.windSpeed = windSpeed;
            weatherInformation.UVIndex = UVIndex;
            weatherInformation.tideEvents = tideEvents;
            return weatherInformation;
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TideEvent {

        @Schema(description = "시간", example = "2021-07-01 00:00:00")
        private LocalDateTime time;

        @Schema(description = "높이", example = "1.00")
        private float height;

        @Schema(description = "조수 타입(만조, 간조)", example = "HIGH")
        private TideType type;

        public static TideEvent of(LocalDateTime time, float height, TideType type) {
            TideEvent tideEvent = new TideEvent();
            tideEvent.time = time;
            tideEvent.height = height;
            tideEvent.type = type;
            return tideEvent;
        }

    }

}

