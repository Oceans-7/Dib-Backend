package com.oceans7.dib.domain.place.service;

import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.response.*;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto.FacilityInfo;
import com.oceans7.dib.global.MockRequest;
import com.oceans7.dib.global.MockResponse;
import com.oceans7.dib.global.api.service.DataGoKrAPIService;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import com.oceans7.dib.global.exception.ApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class PlaceServiceTest {

    @Autowired
    PlaceService placeService;

    @MockBean
    private DataGoKrAPIService tourAPIService;

    @MockBean
    private KakaoLocalAPIService kakaoLocalAPIService;

    @Test
    @DisplayName("관광 정보 리스트 조회 테스트 : 위치 기반")
    public void getPlaceTest() {
        // given
        GetPlaceRequestDto placeReq = MockRequest.testPlaceReq();
        String contentType = String.valueOf(placeReq.getContentType());
        String arrangeType = "";

        // mocking
        when(tourAPIService.getLocationBasedTourApi(
                placeReq.getMapX(), placeReq.getMapY(), placeReq.getPage(), placeReq.getPageSize(), contentType, arrangeType))
                .thenReturn(MockResponse.testPlaceRes());

        // when
        PlaceResponseDto placeRes = placeService.getPlace(placeReq, contentType, arrangeType);
        SimplePlaceInformationDto info = placeRes.getPlaces().get(0);

        // then
        assertThat(placeRes.getPage()).isEqualTo(placeReq.getPage());
        assertThat(placeRes.getPageSize()).isEqualTo(placeReq.getPageSize());
        assertThat(placeRes.getArrangeType()).isNull();

        assertThat(info.getContentId()).isEqualTo(MockRequest.CONTENT_ID);
        assertThat(info.getContentType()).isEqualTo(MockRequest.CONTENT_TYPE);
        assertThat(info.getAddress()).isEqualTo("서울특별시 중구 명동1가 1-3 YWCA연합회");
        assertThat(info.getTel()).isEqualTo("");
        assertThat(info.getTitle()).isEqualTo("뷰티플레이");
        assertThat(info.getFirstImageUrl()).isEqualTo("http://tong.visitkorea.or.kr/cms/resource/49/2947649_image2_1.jpg");
        assertThat(info.getDistance()).isBetween(MockResponse.MIN_DISTANCE, MockResponse.MAX_DISTANCE);
        assertThat(info.getDistance()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("관광 정보 리스트 조회 [정렬] 테스트 : 위치 기반")
    public void getPlaceWithSortingTest() {
        // given
        GetPlaceRequestDto placeWithSortingReq = MockRequest.testPlaceWithSortingReq();
        String contentType = String.valueOf(placeWithSortingReq.getContentType());
        String arrangeType = String.valueOf(placeWithSortingReq.getArrangeType());

        when(tourAPIService.getLocationBasedTourApi(
                placeWithSortingReq.getMapX(), placeWithSortingReq.getMapY(),
                placeWithSortingReq.getPage(), placeWithSortingReq.getPageSize(), contentType, arrangeType))
                .thenReturn(MockResponse.testPlaceRes());

        // when
        PlaceResponseDto placeRes = placeService.getPlace(placeWithSortingReq, contentType, arrangeType);

        // then
        assertThat(placeRes.getPage()).isEqualTo(placeWithSortingReq.getPage());
        assertThat(placeRes.getPageSize()).isEqualTo(placeWithSortingReq.getPageSize());
        assertThat(placeRes.getArrangeType()).isEqualTo(placeWithSortingReq.getArrangeType());

        double cmpDistance = 0.0;
        for(SimplePlaceInformationDto place : placeRes.getPlaces()) {
            assertThat(place.getDistance()).isBetween(MockResponse.MIN_DISTANCE, MockResponse.MAX_DISTANCE);

            // 거리순 정렬 확인
            assertThat(place.getDistance()).isGreaterThanOrEqualTo(cmpDistance);
            cmpDistance = place.getDistance();
        }
    }

    @Test
    @DisplayName("관광 정보 리스트 조회 테스트 : 지역 기반")
    public void getAreaPlaceTest() {
        // given
        GetPlaceRequestDto placeWithAreaReq = MockRequest.testPlaceWithAreaReq();
        String contentType = String.valueOf(placeWithAreaReq.getContentType());
        String arrangeType = "";
        String areaCode = "1";
        String sigunguCode = "24";

        when(tourAPIService.getAreaCodeApi("")).thenReturn(MockResponse.testPlaceAreaCodeRes());
        when(tourAPIService.getAreaCodeApi(areaCode)).thenReturn(MockResponse.testPlaceSigunguCodeRes());
        when(tourAPIService.getAreaBasedTourApi(
                areaCode, sigunguCode, placeWithAreaReq.getPage(), placeWithAreaReq.getPageSize(), contentType, arrangeType))
                .thenReturn(MockResponse.testAreaPlaceRes());

        // when
        PlaceResponseDto placeRes = placeService.getPlace(placeWithAreaReq, contentType, arrangeType);
        SimplePlaceInformationDto info = placeRes.getPlaces().get(0);

        // then
        assertThat(placeRes.getPage()).isEqualTo(placeWithAreaReq.getPage());
        assertThat(placeRes.getPageSize()).isEqualTo(placeWithAreaReq.getPageSize());
        assertThat(placeRes.getArrangeType()).isNull();

        assertThat(info.getContentId()).isEqualTo(MockRequest.CONTENT_ID);
        assertThat(info.getContentType()).isEqualTo(MockRequest.CONTENT_TYPE);
        assertThat(info.getAddress()).isEqualTo("서울특별시 중구 명동1가 1-3 YWCA연합회");
        assertThat(info.getTel()).isEqualTo("");
        assertThat(info.getTitle()).isEqualTo("뷰티플레이");
        assertThat(info.getFirstImageUrl()).isEqualTo("http://tong.visitkorea.or.kr/cms/resource/49/2947649_image2_1.jpg");
        assertThat(info.getAddress().contains(placeWithAreaReq.getArea()) &&
                info.getAddress().contains(placeWithAreaReq.getSigungu())
        ).isTrue();
    }

    @Test
    @DisplayName("[exception] 유효하지 않은 지역 조회 테스트")
    public void getAreaPlaceNotFoundAreaItemExceptionTest() {
        // given
        GetPlaceRequestDto placeWithAreaExceptionReq = MockRequest.testPlaceAreaExceptionReq();
        String contentType = "";
        String arrangeType = "";
        String areaCode = placeWithAreaExceptionReq.getArea();

        when(tourAPIService.getAreaCodeApi("")).thenReturn(MockResponse.testPlaceAreaCodeRes());
        when(tourAPIService.getAreaCodeApi(areaCode)).thenReturn(MockResponse.testPlaceSigunguCodeRes());

        // when & then
        assertThrows(ApplicationException.class, () -> placeService.getPlace(placeWithAreaExceptionReq, contentType, arrangeType));
    }

    @Test
    @DisplayName("관광 정보 [키워드] 검색 테스트")
    public void searchPlaceTest() {
        // given
        SearchPlaceRequestDto searchReq = MockRequest.testSearchReq();
        when(kakaoLocalAPIService.getSearchAddressLocalApi(searchReq.getKeyword()))
                .thenReturn(MockResponse.testSearchNoAddressRes());
        when(tourAPIService.getSearchKeywordTourApi(searchReq.getKeyword(), searchReq.getPage(), searchReq.getPageSize()))
                .thenReturn(MockResponse.testSearchRes());

        // when
        SearchPlaceResponseDto searchRes = placeService.searchPlace(searchReq);
        SimplePlaceInformationDto info = searchRes.getPlaces().get(0);

        // then
        assertThat(searchRes.getPage()).isEqualTo(searchReq.getPage());
        assertThat(searchRes.getPageSize()).isEqualTo(searchReq.getPageSize());
        assertThat(searchRes.getKeyword()).isEqualTo(searchReq.getKeyword());

        assertThat(searchRes.isAreaSearch()).isFalse();
        assertThat(searchRes.getAreas()).isNullOrEmpty();

        assertThat(info.getContentId()).isEqualTo(MockRequest.CONTENT_ID);
        assertThat(info.getContentType()).isEqualTo(MockRequest.CONTENT_TYPE);
        assertThat(info.getAddress()).isEqualTo("서울특별시 중구 명동1가 1-3 YWCA연합회");
        assertThat(info.getTel()).isEqualTo("");
        assertThat(info.getTitle()).isEqualTo("뷰티플레이");
        assertThat(info.getFirstImageUrl()).isEqualTo("http://tong.visitkorea.or.kr/cms/resource/49/2947649_image2_1.jpg");
    }

    @Test
    @DisplayName("[exception] 검색 결과 없음 예외 테스트")
    public void searchPlaceNotFoundItemExceptionTest() {
        // given
        SearchPlaceRequestDto searchNotFoundExceptionReq = MockRequest.testSearchNotFoundExceptionReq();
        when(kakaoLocalAPIService.getSearchAddressLocalApi(searchNotFoundExceptionReq.getKeyword()))
                .thenReturn(MockResponse.testSearchNoAddressRes());
        when(tourAPIService.getSearchKeywordTourApi(searchNotFoundExceptionReq.getKeyword(), searchNotFoundExceptionReq.getPage(), searchNotFoundExceptionReq.getPageSize()))
                .thenReturn(MockResponse.testNoResultRes());

        // then
        assertThrows(ApplicationException.class, () -> placeService.searchPlace(searchNotFoundExceptionReq));
    }

    @Test
    @DisplayName("관광 정보 [지역] 검색 테스트")
    public void searchAreaPlaceTest() {
        // given
        SearchPlaceRequestDto searchAreaReq = MockRequest.testSearchAreaReq();
        when(kakaoLocalAPIService.getSearchAddressLocalApi(searchAreaReq.getKeyword()))
                .thenReturn(MockResponse.testSearchAddressRes());

        // when
        SearchPlaceResponseDto searchRes = placeService.searchPlace(searchAreaReq);
        SimpleAreaResponseDto info = searchRes.getAreas().get(0);

        // then
        assertThat(searchRes.getKeyword()).isEqualTo(searchAreaReq.getKeyword());

        assertThat(searchRes.isAreaSearch()).isTrue();
        assertThat(searchRes.getPlaces()).isNullOrEmpty();

        assertThat(info.getAddress()).isEqualTo("서울 중구");
        assertThat(info.getDistance()).isEqualTo(0.0);
        assertThat(info.getAreaName()).isEqualTo("서울");
        assertThat(info.getSigunguName()).isEqualTo("중구");
        assertThat(info.getMapX()).isEqualTo(126.997555182293);
        assertThat(info.getMapY()).isEqualTo(37.5638077703601);
    }

    @Test
    @DisplayName("관광 정보 상세 조회 테스트")
    public void getPlaceDetailTest() {
        // given
        GetPlaceDetailRequestDto placeDetailReq = MockRequest.testPlaceDetailReq();
        String contentType = String.valueOf(placeDetailReq.getContentType().getCode());

        when(tourAPIService.getCommonApi(placeDetailReq.getContentId(), contentType)).thenReturn(MockResponse.testPlaceCommonRes());
        when(tourAPIService.getIntroApi(placeDetailReq.getContentId(), contentType)).thenReturn(MockResponse.testPlaceIntroRes());
        when(tourAPIService.getInfoApi(placeDetailReq.getContentId(), contentType)).thenReturn(MockResponse.testPlaceInfoRes());
        when(tourAPIService.getImageApi(placeDetailReq.getContentId())).thenReturn(MockResponse.testPlaceImageRes());

        // when
        DetailPlaceInformationResponseDto detailRes = placeService.getPlaceDetail(placeDetailReq);

        // then
        assertThat(detailRes.getContentId()).isEqualTo(MockRequest.CONTENT_ID);
        assertThat(detailRes.getContentType()).isEqualTo(MockRequest.CONTENT_TYPE);
        assertThat(detailRes.getAddress()).isEqualTo("서울특별시 중구 명동1가 1-3 YWCA연합회");
        assertThat(detailRes.getTitle()).isEqualTo("뷰티플레이");
        assertThat(detailRes.getMapX()).isEqualTo(126.997555182293);
        assertThat(detailRes.getMapY()).isEqualTo(37.5638077703601);
        assertThat(detailRes.getIntroduce()).isEqualTo("");
        assertThat(detailRes.getHomepageUrl()).isEqualTo("www.beautyplay.kr");
        assertThat(detailRes.getUseTime()).isEqualTo("10:00~19:00(뷰티 체험은 18:00까지)");
        assertThat(detailRes.getTel()).isEqualTo("070-4070-9675");
        assertThat(detailRes.getRestDate()).isEqualTo("일요일");
        assertThat(detailRes.getReservationUrl()).isNull();

        for(String image : detailRes.getImages()) {
            String urlPattern = "^(https?|ftp)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(/[a-zA-Z0-9-_.]*)+\\.(jpg|jpeg|png|gif|bmp)$";
            assertThat(image.matches(urlPattern)).isEqualTo(true);
        }

        for(FacilityInfo facilityInfo : detailRes.getFacilityInfo()) {
            switch(facilityInfo.getType()) {
                case BABY_CARRIAGE, PET, PARKING, DISABLED_PERSON_FACILITY, CREDIT_CARD -> assertThat(facilityInfo.isAvailability()).isFalse();
                case RESTROOM -> assertThat(facilityInfo.isAvailability()).isTrue();
            }
        }
    }
}
