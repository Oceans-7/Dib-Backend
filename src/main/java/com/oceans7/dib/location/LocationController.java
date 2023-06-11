package com.oceans7.dib.location;

import com.oceans7.dib.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.location.dto.response.LocationResponseDto;
import com.oceans7.dib.place.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "location", description = "주소 검색 API")
@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "위도, 경도 검색", description = "주소를 받아 해당 지역의 위도와 경도를 조회한다.")
    public ResponseEntity<LocationResponseDto> searchPlace(
            @Validated @ModelAttribute SearchLocationRequestDto searchLocationRequestDto
    ) {
        {
            return null;
        }
    }
}
