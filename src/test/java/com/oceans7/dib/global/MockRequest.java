package com.oceans7.dib.global;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;

public class MockRequest {
    public static final String KEYWORD_QUERY = "뷰티플레이";
    public static final String AREA_QUERY = "서울 중구";

    public static final Long CONTENT_ID = (long) 2946230;
    public static final ContentType CONTENT_TYPE = ContentType.TOURIST_SPOT;
    public static final ArrangeType ARRANGE_TYPE = ArrangeType.E;

    public static final double X = 126.997555182293;
    public static final double Y = 37.5638077703601;

    public static GetPlaceRequestDto testPlaceReq() {
        return new GetPlaceRequestDto(X, Y, CONTENT_TYPE, null, null, null, 1, 2);
    }

    public static GetPlaceRequestDto testPlaceWithSortingReq() {
        return new GetPlaceRequestDto(X, Y, CONTENT_TYPE, null, null, ARRANGE_TYPE, 1, 2);
    }

    public static GetPlaceRequestDto testPlaceWithAreaReq() {
        return new GetPlaceRequestDto(X, Y, CONTENT_TYPE,
                AREA_QUERY.split(" ")[0], AREA_QUERY.split(" ")[1], null, 1, 1);
    }

    public static GetPlaceRequestDto testPlaceXYExceptionReq() {
        return new GetPlaceRequestDto(0, 0, null, null, null, null, 1, 1);
    }

    public static GetPlaceRequestDto testPlaceAreaExceptionReq() {
        return new GetPlaceRequestDto(X, Y, null, "Invalid Area Name", null, null, 1, 1);
    }

    public static SearchPlaceRequestDto testSearchReq() {
        return new SearchPlaceRequestDto(KEYWORD_QUERY, X, Y, 1, 1);
    }

    public static SearchPlaceRequestDto testSearchAreaReq() {
        return new SearchPlaceRequestDto(AREA_QUERY, X, Y, 1, 1);
    }

    public static SearchPlaceRequestDto testSearchNotFoundExceptionReq() {
        return new SearchPlaceRequestDto("Not Found Keyword!!", X, Y, 1, 1);
    }

    public static GetPlaceDetailRequestDto testPlaceDetailReq() {
        return new GetPlaceDetailRequestDto(CONTENT_ID, CONTENT_TYPE);
    }

    public static SearchLocationRequestDto testSearchLocationReq() {
        return new SearchLocationRequestDto(X, Y);
    }

    public static SearchLocationRequestDto testSearchLocationXYExceptionReq() {
        return new SearchLocationRequestDto(0, 0);
    }
}
