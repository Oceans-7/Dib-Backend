package com.oceans7.dib.global;

import com.oceans7.dib.domain.event.entity.*;
import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.mypage.dto.request.UpdateProfileRequestDto;
import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.entity.Dib;
import com.oceans7.dib.domain.user.entity.Role;
import com.oceans7.dib.domain.user.entity.SocialType;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user_refresh_token.entity.UserRefreshToken;
import com.oceans7.dib.global.util.CoordinateUtil;

import java.time.LocalDate;

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

    public static User testUser() {
        return User.of("profile_img", "oceans", SocialType.KAKAO, "dib123", Role.USER);
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

    public static int testBaseX() {
        return (int) testBaseXY().x;
    }

    public static int testBaseY() {
        return (int) testBaseXY().y;
    }

    private static CoordinateUtil.LatXLngY testBaseXY() {
        return CoordinateUtil.convertGRID_GPS(X, Y);
    }

    public static Event testEvent() {
        return Event.of("FF0770EF", "FFEBF4FE", "https://picsum.photos/150/190");
    }

    public static CouponGroup testCouponGroup() {
        return CouponGroup.of("제주 서귀포 숙박 할인권", "제주 서귀포", CouponType.ACCOMMODATION, "1234", 10, LocalDate.now(), LocalDate.now().plusMonths(1), "https://picsum.photos/150/190", "https://picsum.photos/150/190");
    }

    public static CouponGroup testCouponGroup2() {
        return CouponGroup.of("제주 서귀포 다이빙 체험 할인권", "제주 서귀포", CouponType.SCUBA_DIVING, "1234", 10, LocalDate.now(), LocalDate.now().plusMonths(1), "https://picsum.photos/150/190", "https://picsum.photos/150/190");
    }

    public static Coupon testCoupon() {
        return Coupon.of(LocalDate.now(), UseStatus.UNUSED);
    }

    public static Dib testDib(User user) {
        return Dib.of(CONTENT_ID, CONTENT_TYPE.getCode(), "뷰티플레이", "서울특별시 중구 명동1가 1-3 YWCA연합회", "070-4070-9675", "http://tong.visitkorea.or.kr/cms/resource/49/2947649_image2_1.jpg", user);
    }

    public static UpdateProfileRequestDto testUpdateProfileReq() {
        return new UpdateProfileRequestDto("변경 닉네임", "http://tong.visitkorea.or.kr/cms/resource/49/2947649_image2_1.jpg");
    }

    public static UserRefreshToken testUserRefreshToken(User user, String refreshToken) {
        return UserRefreshToken.of(refreshToken, user);
    }
}
