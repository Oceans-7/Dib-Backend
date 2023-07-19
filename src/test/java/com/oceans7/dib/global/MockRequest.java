package com.oceans7.dib.global;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;

public class MockRequest {
    private static final double MAP_X = 126.9779692;
    private static final double MAP_Y = 37.566535;

    public static GetPlaceRequestDto testGetPlaceReqNoOption() {
        return new GetPlaceRequestDto(MAP_X, MAP_Y,
                ContentType.TOURIST_SPOT, null, null, null, 1, 10);
    }

    public static GetPlaceRequestDto testGetPlaceWithSortingReq() {
        return new GetPlaceRequestDto(MAP_X, MAP_Y,
                ContentType.TOURIST_SPOT, "", "", ArrangeType.DISTANCE, 1, 10);
    }

    public static GetPlaceRequestDto testGetAreaPlaceReq() {
        return new GetPlaceRequestDto(MAP_X, MAP_Y,
                ContentType.TOURIST_SPOT, "서울", "성북구", null, 1, 10);
    }

    public static SearchPlaceRequestDto testSearchPlaceReq() {
        return new SearchPlaceRequestDto("식당", MAP_X, MAP_Y, 1, 10);
    }

    public static SearchPlaceRequestDto testSearchAreaPlaceReq() {
        return new SearchPlaceRequestDto("고성", MAP_X, MAP_Y, 1, 10);
    }

    public static GetPlaceDetailRequestDto testGetPlaceDetailReq() {
        return new GetPlaceDetailRequestDto((long)2592089, ContentType.TOURIST_SPOT);
    }

    public static SearchLocationRequestDto testSearchLocationReq() {
        return new SearchLocationRequestDto(126.9779692, 37.566535);
    }

    public static SearchLocationRequestDto testSearchLocationXYExceptionReq() {
        return new SearchLocationRequestDto(0, 0);
    }
}
