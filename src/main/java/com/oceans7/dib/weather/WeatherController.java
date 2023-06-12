package com.oceans7.dib.weather;

import com.oceans7.dib.weather.dto.request.GetLocationWeatherRequestDto;
import com.oceans7.dib.weather.dto.response.GetLocationWeatherResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "weather", description = "날씨 정보 API")
@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @Operation(summary = "위치 기반 날씨 정보 조회", description = "위도와 경도를 받아 해당 지역의 날씨 정보를 조회한다.")
    @GetMapping()
    public ResponseEntity<GetLocationWeatherResponseDto> getWeather(
            @Validated @ModelAttribute GetLocationWeatherRequestDto getLocationWeatherRequestDto
    ) {
            return ResponseEntity.ok(weatherService.getWeather(getLocationWeatherRequestDto))
    }
}
