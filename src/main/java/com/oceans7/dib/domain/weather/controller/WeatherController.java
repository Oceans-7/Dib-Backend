package com.oceans7.dib.domain.weather.controller;

import com.oceans7.dib.domain.weather.dto.response.GetCurrentLocationWeatherResponseDto;
import com.oceans7.dib.domain.weather.service.WeatherService;
import com.oceans7.dib.domain.weather.dto.request.GetLocationWeatherRequestDto;
import com.oceans7.dib.domain.weather.dto.response.GetLocationWeatherResponseDto;
import com.oceans7.dib.global.exception.ErrorResponse;
import com.oceans7.dib.global.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
    @GetMapping("/current")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "W0000", description = "날씨 정보를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "O0001", description = "Open API 서버 연결에 실패하였습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ApplicationResponse<GetCurrentLocationWeatherResponseDto> getWeather(
            @ParameterObject @ModelAttribute @Validated GetLocationWeatherRequestDto getLocationWeatherRequestDto
    ) {
        GetCurrentLocationWeatherResponseDto weather = weatherService.getWeather(getLocationWeatherRequestDto);
        return ApplicationResponse.ok(weather);
    }
}
