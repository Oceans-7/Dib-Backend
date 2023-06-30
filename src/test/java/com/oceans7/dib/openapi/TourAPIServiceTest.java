package com.oceans7.dib.openapi;

import com.oceans7.dib.domain.place.ArrangeType;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.openapi.dto.response.detail.common.DetailCommonListResponse;
import com.oceans7.dib.openapi.dto.response.detail.image.DetailImageListResponse;
import com.oceans7.dib.openapi.dto.response.detail.info.DetailInfoListResponse;
import com.oceans7.dib.openapi.dto.response.detail.intro.*;
import com.oceans7.dib.openapi.dto.response.detail.intro.DetailIntroResponse.*;
import com.oceans7.dib.openapi.dto.response.list.AreaCodeList;
import com.oceans7.dib.openapi.dto.response.list.TourAPICommonListResponse;
import com.oceans7.dib.openapi.service.TourAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TourAPIServiceTest {
    @Autowired
    TourAPIService tourAPIService;

    @Test
    @DisplayName("위치 기반 서비스 API 통신 테스트")
    public void callLocationBasedAPITest() {
        // given
        double mapX = 126.9779692;
        double mapY = 37.566535;
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());
        String arrangeTypeName = ArrangeType.DISTANCE.getCode();

        int page = 1;
        int pageSize = 20;

        // when
        TourAPICommonListResponse list = tourAPIService.fetchDataFromLocationBasedApi(mapX, mapY, contentTypeId, arrangeTypeName, page, pageSize);

        // then
        assertThat(list.getTourAPICommonItemResponseList().size()).isEqualTo(pageSize);
        assertThat(list.getPage()).isEqualTo(page);
        assertThat(list.getPageSize()).isEqualTo(pageSize);
    }

    @Test
    @DisplayName("키워드 검색 서비스 API 통신 테스트")
    public void callSearchKeywordAPITest() {
        // given
        String keyword = "강원";

        // when
        TourAPICommonListResponse list = tourAPIService.fetchDataFromSearchKeywordApi(keyword, "", "", "", "");

        // then
        assertThat(list.getTourAPICommonItemResponseList().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("지역 코드 조회 API 통신 테스트")
    public void callAreaCodeAPITest() {
        String areaCode = "1"; //seoul

        AreaCodeList areaList = tourAPIService.fetchDataFromAreaCodeApi("");
        AreaCodeList areaDepthList = tourAPIService.fetchDataFromAreaCodeApi(areaCode);

        assertThat(areaList.getAreaCodeItems().size()).isEqualTo(17);
        assertThat(areaDepthList.getAreaCodeItems().size()).isEqualTo(25);
    }

    @Test
    @DisplayName("지역 기반 관광정보 조회 API 통신 테스트")
    public void callAreaBasedAPITest() {
        // given : '서울시 강북구' 지역
        String areaCode = "1";
        String sigunguCode = "3";
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());
        String arrangeTypeName = ArrangeType.TITLE.getCode();

        TourAPICommonListResponse tourAPICommonList = tourAPIService.fetchDataFromAreaBasedApi(areaCode, sigunguCode, contentTypeId, arrangeTypeName);

        assertThat(tourAPICommonList.getTourAPICommonItemResponseList().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("공통 정보 조회 API 통신 테스트")
    public void callDetailCommonAPITest() {
        //given
        String keyword = "서울";
        String areaCode = "1";
        String sigunguCode = "3";
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());
        String arrangeTypeName = ArrangeType.TITLE.getCode();

        TourAPICommonListResponse list = tourAPIService.fetchDataFromSearchKeywordApi(keyword, areaCode, sigunguCode, contentTypeId, arrangeTypeName);
        Long contentId = list.getTourAPICommonItemResponseList().get(0).getContentId();

        // when
        DetailCommonListResponse detailCommonList = tourAPIService.fetchDataFromCommonApi(contentId, contentTypeId);

        //then
        assertThat(detailCommonList.getDetailCommonItemResponse().getContentId()).isEqualTo(contentId);
        assertThat(detailCommonList.getDetailCommonItemResponse().getContentTypeId()).isEqualTo(Integer.parseInt(contentTypeId));
    }

    @Test
    @DisplayName("소개 정보 조회 API 통신 테스트")
    public void callDetailIntroAPITest() {
        //given
        String keyword = "강원";
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());

        TourAPICommonListResponse list = tourAPIService.fetchDataFromSearchKeywordApi(keyword, "", "", contentTypeId, "");
        Long contentId = list.getTourAPICommonItemResponseList().get(0).getContentId();

        // when
        DetailIntroResponse detailIntroItem = tourAPIService.fetchDataFromIntroApi(contentId, contentTypeId);

        // then
        if(detailIntroItem instanceof SpotIntroResponse) {
            SpotIntroResponse result = (SpotIntroResponse) detailIntroItem;

            assertThat(result.getSpotItemResponses().getContentId()).isEqualTo(contentId);
            assertThat(result.getSpotItemResponses().getContentTypeId()).isEqualTo(Integer.parseInt(contentTypeId));
        } else if(detailIntroItem instanceof CultureIntroResponse) {
            CultureIntroResponse result = (CultureIntroResponse) detailIntroItem;

            assertThat(result.getCultureItemResponse().getContentId()).isEqualTo(contentId);
            assertThat(result.getCultureItemResponse().getContentTypeId()).isEqualTo(Integer.parseInt(contentTypeId));
        }
        else if(detailIntroItem instanceof EventIntroResponse) {
            EventIntroResponse result = (EventIntroResponse) detailIntroItem;

            assertThat(result.getEventItemResponse().getContentId()).isEqualTo(contentId);
            assertThat(result.getEventItemResponse().getContentTypeId()).isEqualTo(Integer.parseInt(contentTypeId));
        } else if(detailIntroItem instanceof LeportsIntroResponse) {
            LeportsIntroResponse result = (LeportsIntroResponse) detailIntroItem;

            assertThat(result.getLeportsItemResponse().getContentId()).isEqualTo(contentId);
            assertThat(result.getLeportsItemResponse().getContentTypeId()).isEqualTo(Integer.parseInt(contentTypeId));
        } else if(detailIntroItem instanceof AccommodationIntroResponse) {
            AccommodationIntroResponse result = (AccommodationIntroResponse) detailIntroItem;

            assertThat(result.getAccommodationItemResponse().getContentId()).isEqualTo(contentId);
            assertThat(result.getAccommodationItemResponse().getContentTypeId()).isEqualTo(Integer.parseInt(contentTypeId));
        } else if(detailIntroItem instanceof ShoppingIntroResponse) {
            ShoppingIntroResponse result = (ShoppingIntroResponse) detailIntroItem;

            assertThat(result.getShoppingItemResponse().getContentId()).isEqualTo(contentId);
            assertThat(result.getShoppingItemResponse().getContentTypeId()).isEqualTo(Integer.parseInt(contentTypeId));
        } else if(detailIntroItem instanceof RestaurantIntroResponse) {
            RestaurantIntroResponse result = (RestaurantIntroResponse) detailIntroItem;

            assertThat(result.getRestaurantItemResponse().getContentId()).isEqualTo(contentId);
            assertThat(result.getRestaurantItemResponse().getContentTypeId()).isEqualTo(Integer.parseInt(contentTypeId));
        }
    }

    @Test
    @DisplayName("반복 정보 조회 API 통신 테스트")
    public void callDetailInfoAPITest() {
        //given
        String keyword = "백범김구선생상";

        TourAPICommonListResponse list = tourAPIService.fetchDataFromSearchKeywordApi(keyword, "", "", "", "");
        Long contentId = list.getTourAPICommonItemResponseList().get(0).getContentId();
        String contentTypeId = String.valueOf(
                list.getTourAPICommonItemResponseList().get(0).getContentTypeId()
        );

        //then
        DetailInfoListResponse detailInfoListResponse = tourAPIService.fetchDataFromInfoApi(contentId, contentTypeId);

        // when
        assertThat(detailInfoListResponse.getDetailInfoItemResponses().get(0).getContentId()).isEqualTo(contentId);
        assertThat(detailInfoListResponse.getDetailInfoItemResponses().get(0).getContentTypeId()).isEqualTo(Integer.parseInt(contentTypeId));
    }

    @Test
    @DisplayName("이미지 조회 API 통신 테스트")
    public void callImageAPITest() {
        // given
        Long contentId = (long) 128553;

        // then
        DetailImageListResponse detailImageListResponse = tourAPIService.fetchImageDataFromApi(contentId);

        // when
        assertThat(detailImageListResponse.getDetailImageItemResponses().get(0).getContentId()).isEqualTo(contentId);
        assertThat(detailImageListResponse.getDetailImageItemResponses().size()).isEqualTo(10);
        String urlPattern = "^(https?|ftp)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(/[a-zA-Z0-9-_.]*)+\\.(jpg|jpeg|png|gif)$";

        assertThat(detailImageListResponse.getDetailImageItemResponses().get(0).getOriginImageUrl().matches(urlPattern)).isEqualTo(true);

    }
}
