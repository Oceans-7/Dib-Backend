package com.oceans7.dib.global;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.mypage.dto.request.UpdateProfileRequestDto;
import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.dto.PlaceFilterOptions;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.report.dto.request.ReportRequestDto;
import com.oceans7.dib.global.util.CoordinateUtil;

import java.util.ArrayList;
import java.util.List;

public class MockRequest {
    public static final String KEYWORD_QUERY = "뷰티플레이";
    public static final String AREA_QUERY = "서울 중구";

    public static final Long CONTENT_ID = (long) 2946230;
    public static final ContentType CONTENT_TYPE = ContentType.TOURIST_SPOT;
    public static final ArrangeType ARRANGE_TYPE = ArrangeType.E;

    public final static String YES_OPTION = "Y";
    public final static int RADIUS = 20000;
    public final static int MAX_AREA_CODE_SIZE = 50;

    public static final double X = 126.997555182293;
    public static final double Y = 37.5638077703601;

    public final static int NCST_CALLABLE_TIME = 40;
    public final static int FCST_CALLABLE_TIME = 60;

    public final static int BASE_PAGE = 1;
    public final static int NCST_PAGE_SIZE = 8;
    public final static int FCST_PAGE_SIZE = 60;

    public static PlaceFilterOptions testPlaceFilterOptionReq(GetPlaceRequestDto testPlaceReq) {
        PlaceFilterOptions filterOption = PlaceFilterOptions.initialBuilder()
                .request(testPlaceReq)
                .build();

        return filterOption;
    }

    public static PlaceFilterOptions testPlaceFilterOptionReq(GetPlaceRequestDto testPlaceReq) {
        PlaceFilterOptions filterOption = PlaceFilterOptions.initialBuilder()
                .request(testPlaceReq)
                .build();

        return filterOption;
    }

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
        return SearchPlaceRequestDto.builder()
                .keyword(KEYWORD_QUERY)
                .mapX(X)
                .mapY(Y)
                .page(1)
                .pageSize(1)
                .build();
    }

    public static SearchPlaceRequestDto testSearchAreaReq() {
        return SearchPlaceRequestDto.builder()
                .keyword(AREA_QUERY)
                .mapX(X)
                .mapY(Y)
                .page(1)
                .pageSize(1)
                .build();
    }

    public static SearchPlaceRequestDto testSearchNotFoundExceptionReq() {
        return SearchPlaceRequestDto.builder()
                .keyword("Not Found Keyword!!")
                .mapX(X)
                .mapY(Y)
                .page(1)
                .pageSize(1)
                .build();
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

    public static int testBaseX() {
        return (int) testBaseXY().x;
    }

    public static int testBaseY() {
        return (int) testBaseXY().y;
    }

    private static CoordinateUtil.LatXLngY testBaseXY() {
        return CoordinateUtil.convertGRID_GPS(X, Y);
    }

    public static UpdateProfileRequestDto testUpdateProfileReq() {
        return new UpdateProfileRequestDto("변경 닉네임", "http://tong.visitkorea.or.kr/cms/resource/49/2947649_image2_1.jpg");
    }

    public static ReportRequestDto testReportReq() {
        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add("https://images/2");
        return new ReportRequestDto("갯주풀 / 영국 갯끈풀", "강원 강릉시 강문동", imageUrlList);
    }
}
