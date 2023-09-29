package com.oceans7.dib.domain.weather.service.vo;


import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.domain.weather.dto.response.TideEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class WeatherVO {

    private LocalDate date;

    private WeatherType weatherType;

    private Double airTemperature;

    private Double waterTemperature;

    private Double windSpeed;

    private DivingIndicator divingIndicator;

    private List<TideEvent> tideEvents;

    private Double waveHeight;
}
