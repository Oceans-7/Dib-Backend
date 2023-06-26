package com.oceans7.dib.domain.place;

import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto;
import com.oceans7.dib.domain.place.dto.response.PlaceResponseDto;
import com.oceans7.dib.domain.place.dto.response.SearchPlaceResponseDto;
import com.oceans7.dib.global.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "place", description = "관광 정보 API")
@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @Operation(summary = "관광 정보 조회", description = "사용자 위치(위도, 경도) 기반 관련 정보를 조회한다. (옵션 : 필터링 적용) \n지역 필터링 적용시 사용자 위치는 무시됩니다.")
    @GetMapping()
    public ApplicationResponse<PlaceResponseDto> searchPlace(@ModelAttribute GetPlaceRequestDto placeRequestDto) {
        {
            return ApplicationResponse.ok(placeService.getPlace(placeRequestDto));
        }
    }

    @Operation(summary = "키워드로 관광 정보 검색", description = "키워드를 입력받아 관련 정보를 조회한다. (옵션 : 필터링 적용)")
    @GetMapping("/search")
    public ApplicationResponse<SearchPlaceResponseDto> searchPlace(@ModelAttribute SearchPlaceRequestDto searchPlaceRequestDto) {
        {
            return ApplicationResponse.ok(placeService.searchPlace(searchPlaceRequestDto));
        }
    }

    @Operation(summary = "관광 정보 상세 조회", description = "콘텐츠 ID와 콘텐츠 타입을 입력 받아 관광 상세 정보를 조회한다.")
    @GetMapping("/detail")
    public ApplicationResponse<DetailPlaceInformationResponseDto> getPlaceDetail(@ModelAttribute GetPlaceDetailRequestDto getPlaceDetailRequestDto) {
        {
            return ApplicationResponse.ok(placeService.getPlaceDetail(getPlaceDetailRequestDto));
        }
    }
}
