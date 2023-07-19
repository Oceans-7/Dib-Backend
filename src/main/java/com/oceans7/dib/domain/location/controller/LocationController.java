package com.oceans7.dib.domain.location.controller;

import com.oceans7.dib.domain.location.service.LocationService;
import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.location.dto.response.LocationResponseDto;
import com.oceans7.dib.global.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "location", description = "주소 검색 API")
@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping()
    @Operation(summary = "좌표 기준 주소 및 날씨 조회", description = "위도, 경도를 받아 해당 지역의 도로명 주소와 기온 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "O0001", description = "Open API 서버 연결에 실패하였습니다.", content = @Content(schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "C0005", description = "유효성 검사를 실패했습니다.", content = @Content(schema = @Schema(implementation = ApplicationResponse.class))),
    })
    public ApplicationResponse<LocationResponseDto> searchPlace(
            @Validated @ModelAttribute SearchLocationRequestDto searchLocationRequestDto
    ) {
        return ApplicationResponse.ok(locationService.searchPlace(searchLocationRequestDto));
    }
}
