package com.oceans7.dib.domain.place.service;

import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.response.*;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto.FacilityInfo;
import com.oceans7.dib.global.MockRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class PlaceServiceTest {

    @Autowired
    PlaceService placeService;

    private GetPlaceRequestDto placeReqDto;
    private GetPlaceRequestDto placeWithSortingReqDto;
    private GetPlaceRequestDto areaPlaceReq;

    private SearchPlaceRequestDto searchPlaceReqDto;
    private SearchPlaceRequestDto searchAreaPlaceReqDto;

    private GetPlaceDetailRequestDto getPlaceDetailReqDto;

    private final static double MIN_DISTANCE = 0.0;
    private final static double MAX_DISTANCE = 20.0;

    @BeforeEach
    public void before() {
        placeReqDto = MockRequest.testGetPlaceReqNoOption();
        placeWithSortingReqDto = MockRequest.testGetPlaceWithSortingReq();
        areaPlaceReq = MockRequest.testGetAreaPlaceReq();

        searchPlaceReqDto = MockRequest.testSearchPlaceReq();
        searchAreaPlaceReqDto = MockRequest.testSearchAreaPlaceReq();

        getPlaceDetailReqDto = MockRequest.testGetPlaceDetailReq();
    }

    @Test
    @DisplayName("관광 정보 리스트 조회 테스트 : 위치 기반")
    public void getPlaceTest() {
        // given
        String contentType = String.valueOf(placeReqDto.getContentType().getCode());
        String arrangeType = "";

        // when
        PlaceResponseDto placeResDto = placeService.getPlace(placeReqDto, contentType, arrangeType);

        // then
        assertThat(placeResDto.getPage()).isEqualTo(placeReqDto.getPage());
        assertThat(placeResDto.getPageSize()).isEqualTo(placeReqDto.getPageSize());
        assertThat(placeResDto.getArrangeType()).isNull();

        for(SimplePlaceInformationDto place : placeResDto.getPlaces()) {
            assertThat(place.getContentType()).isEqualTo(placeReqDto.getContentType());
            assertThat(place.getDistance()).isBetween(0.0, 20.0);
        }
    }

    @Test
    @DisplayName("관광 정보 리스트 조회 [정렬] 테스트 : 위치 기반")
    public void getPlaceWithSortingTest() {
        // given
        String contentType = String.valueOf(placeWithSortingReqDto.getContentType().getCode());
        String arrangeType = placeWithSortingReqDto.getArrangeType().name();

        // when
        PlaceResponseDto placeResDto = placeService.getPlace(placeWithSortingReqDto, contentType, arrangeType);

        // then
        assertThat(placeResDto.getPage()).isEqualTo(placeWithSortingReqDto.getPage());
        assertThat(placeResDto.getPageSize()).isEqualTo(placeWithSortingReqDto.getPageSize());
        assertThat(placeResDto.getArrangeType()).isEqualTo(placeWithSortingReqDto.getArrangeType());

        double cmpDistance = 0.0;
        for(SimplePlaceInformationDto place : placeResDto.getPlaces()) {
            assertThat(place.getContentType()).isEqualTo(placeWithSortingReqDto.getContentType());
            assertThat(place.getDistance()).isBetween(MIN_DISTANCE, MAX_DISTANCE);

            // 거리순 정렬 확인
            assertThat(place.getDistance()).isGreaterThanOrEqualTo(cmpDistance);
            cmpDistance = place.getDistance();
        }
    }

    @Test
    @DisplayName("관광 정보 리스트 조회 테스트 : 지역 기반")
    public void getAreaPlaceTest() {
        // given
        String contentType = String.valueOf(placeReqDto.getContentType().getCode());
        String arrangeType = "";

        // when
        PlaceResponseDto placeResDto = placeService.getPlace(areaPlaceReq, contentType, arrangeType);

        // then
        assertThat(placeResDto.getPage()).isEqualTo(areaPlaceReq.getPage());
        assertThat(placeResDto.getPageSize()).isEqualTo(areaPlaceReq.getPageSize());
        assertThat(placeResDto.getArrangeType()).isNull();

        for(SimplePlaceInformationDto place : placeResDto.getPlaces()) {
            assertThat(place.getContentType()).isEqualTo(placeReqDto.getContentType());
            assertThat(place.getDistance()).isBetween(MIN_DISTANCE, MAX_DISTANCE);
            assertThat(place.getAddress()).contains(areaPlaceReq.getArea());
            assertThat(place.getAddress()).contains(areaPlaceReq.getSigungu());
        }
    }

    @Test
    @DisplayName("관광 정보 키워드 검색 테스트")
    public void searchPlaceTest() {
        // given : "식당"

        // when
        SearchPlaceResponseDto searchPlaceResDto = placeService.searchPlace(searchPlaceReqDto);

        // then
        assertThat(searchPlaceResDto.getPage()).isEqualTo(searchPlaceReqDto.getPage());
        assertThat(searchPlaceResDto.getPageSize()).isEqualTo(searchPlaceReqDto.getPageSize());
        assertThat(searchPlaceResDto.getKeyword()).isEqualTo(searchPlaceReqDto.getKeyword());

        assertThat(searchPlaceResDto.isAreaSearch()).isFalse();
        assertThat(searchPlaceResDto.getAreas()).isNullOrEmpty();

        assertThat(searchPlaceResDto.getPlaces().length).isEqualTo(searchPlaceReqDto.getPageSize());
    }

    @Test
    @DisplayName("관광 정보 지역 검색 테스트")
    public void searchAreaPlaceTest() {
        // given : "고성"

        // when
        SearchPlaceResponseDto searchPlaceResDto = placeService.searchPlace(searchAreaPlaceReqDto);

        assertThat(searchPlaceResDto.getKeyword()).isEqualTo(searchAreaPlaceReqDto.getKeyword());

        assertThat(searchPlaceResDto.isAreaSearch()).isTrue();
        assertThat(searchPlaceResDto.getPlaces()).isNullOrEmpty();

        for(SimpleAreaResponseDto areaItem : searchPlaceResDto.getAreas()) {
            assertThat(areaItem.getAddress().contains(searchAreaPlaceReqDto.getKeyword())).isTrue();
        }
    }

    @Test
    @DisplayName("관광 정보 상세 조회 테스트")
    public void getPlaceDetailTest() {
        // given - contentid :2592089
        String title = "문화철도 959";
        String address = "서울특별시 구로구 경인로 688 (신도림동)";
        double mapX = 126.8912917045;
        double mapY = 37.5089677083;
        String homepageUrl = "http://art959.com/";
        String restDate = "대관 (매주 일요일, 공휴일)\n카페 (매주 일요일, 월요일)";
        String useTime = "[대관]\n주중 09:00~22:00\n토요일 10:00~17:00\n\n[키즈카페]\n화요일~금요일 10:00~19:00\n토요일 / 공휴일 10:00~18:00";
        String tel = "02-856-1702";
        String urlPattern = "^(https?|ftp)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(/[a-zA-Z0-9-_.]*)+\\.(jpg|jpeg|png|gif|bmp)$";

        // when
        DetailPlaceInformationResponseDto detailResDto = placeService.getPlaceDetail(getPlaceDetailReqDto);

        // then
        assertThat(detailResDto.getContentId()).isEqualTo(getPlaceDetailReqDto.getContentId());
        assertThat(detailResDto.getContentType()).isEqualTo(getPlaceDetailReqDto.getContentType());
        assertThat(detailResDto.getTitle()).isEqualTo(title);
        assertThat(detailResDto.getAddress()).isEqualTo(address);
        assertThat(detailResDto.getMapX()).isEqualTo(mapX);
        assertThat(detailResDto.getMapY()).isEqualTo(mapY);
        assertThat(detailResDto.getHomepageUrl()).isEqualTo(homepageUrl);
        assertThat(detailResDto.getUseTime()).isEqualTo(useTime);
        assertThat(detailResDto.getRestDate()).isEqualTo(restDate);
        assertThat(detailResDto.getTel()).isEqualTo(tel);

        for(String image : detailResDto.getImages()) {
            assertThat(image.matches(urlPattern)).isEqualTo(true);
        }

        for(FacilityInfo facilityInfo : detailResDto.getFacilityInfo()) {
            switch(facilityInfo.getType()) {
                case BABY_CARRIAGE, PET, PARKING, DISABLED_PERSON_FACILITY -> assertThat(facilityInfo.isAvailability()).isFalse();
                case CREDIT_CARD, RESTROOM -> assertThat(facilityInfo.isAvailability()).isTrue();
            }
        }
    }
}
