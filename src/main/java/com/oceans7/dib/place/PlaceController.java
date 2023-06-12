package com.oceans7.dib.place;

import com.oceans7.dib.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.place.dto.response.DetailPlaceInformationResponseDto;
import com.oceans7.dib.place.dto.response.SearchPlaceResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "place", description = "관광 정보 API")
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    public ResponseEntity<SearchPlaceResponseDto> searchPlace(@ModelAttribute SearchPlaceRequestDto searchPlaceRequestDto) {
        {
            return null;
        }
    }

    public ResponseEntity<DetailPlaceInformationResponseDto> getPlaceDetail(@ModelAttribute GetPlaceDetailRequestDto getPlaceDetailRequestDto) {
        {
            return null;
        }
    }
}
