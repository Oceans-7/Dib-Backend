package com.oceans7.dib.domain.weather;

import com.oceans7.dib.domain.weather.dto.request.GetLocationWeatherRequestDto;
import com.oceans7.dib.domain.weather.dto.response.GetLocationWeatherResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {
    public GetLocationWeatherResponseDto getWeather(GetLocationWeatherRequestDto getLocationWeatherRequestDto) {
        return null;
    }
}
