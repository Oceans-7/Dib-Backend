package com.oceans7.dib.openapi;

import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.domain.place.dto.ContentType;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroResponse.*;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroItemResponse.*;
import com.oceans7.dib.global.api.response.tourapi.list.AreaCodeList;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonItemResponse;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonListResponse;
import com.oceans7.dib.global.api.service.DataGoKrAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class DataGoKrAPIServiceTest {
    @Autowired
    DataGoKrAPIService dataGoKrAPIService;

    @Test
    @DisplayName("위치 기반 서비스 API 통신 테스트")
    public void callLocationBasedAPITest() {
        // given
        double mapX = 126.9779692;
        double mapY = 37.566535;
        int page = 1;
        int pageSize = 4;
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());
        String arrangeType = ArrangeType.DISTANCE.getCode();

        // when
        TourAPICommonListResponse list =
                dataGoKrAPIService.getLocationBasedTourApi(mapX, mapY, page, pageSize, contentTypeId, arrangeType);

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

        int page = 1;
        int pageSize = 4;

        // when
        TourAPICommonListResponse list =
                dataGoKrAPIService.getSearchKeywordTourApi(keyword, page, pageSize, "", "", "", "");

        // then
        List<TourAPICommonItemResponse> itemList = list.getTourAPICommonItemResponseList();
        for(TourAPICommonItemResponse item : itemList) {
            assertThat(item.getTitle().contains(keyword)).isEqualTo(true);
        }
    }

    @Test
    @DisplayName("지역 코드 조회 API 통신 테스트")
    public void callAreaCodeAPITest() {
        // given : seoul
        String areaCode = "1";

        // when
        AreaCodeList areaList = dataGoKrAPIService.getAreaCodeApi("");
        AreaCodeList areaDepthList = dataGoKrAPIService.getAreaCodeApi(areaCode);

        // then
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
        int page = 1;
        int pageSize = 4;
        String arrangeType = ArrangeType.TITLE.getCode();

        // when
        TourAPICommonListResponse tourAPICommonList =
                dataGoKrAPIService.getAreaBasedTourApi(areaCode, sigunguCode, page, pageSize, contentTypeId, arrangeType);

        // than
        List<TourAPICommonItemResponse> itemList = tourAPICommonList.getTourAPICommonItemResponseList();

        for(TourAPICommonItemResponse item : itemList) {
            assertThat(item.getAddress1().contains("서울")
                    && item.getAddress1().contains("강북"))
                    .isEqualTo(true);
        }
    }

    @Test
    @DisplayName("공통 정보 조회 API 통신 테스트")
    public void callDetailCommonAPITest() {
        //given
        String areaCode = "1";
        String sigunguCode = "3";
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());
        int page = 1;
        int pageSize = 4;
        String arrangeType = ArrangeType.TITLE.getCode();

        TourAPICommonListResponse tourAPICommonList =
                dataGoKrAPIService.getAreaBasedTourApi(areaCode, sigunguCode, page, pageSize, contentTypeId, arrangeType);
        TourAPICommonItemResponse compareItem = tourAPICommonList.getTourAPICommonItemResponseList().get(0);

        // when
        DetailCommonListResponse detailCommonList = dataGoKrAPIService.getCommonApi(compareItem.getContentId(), contentTypeId);
        DetailCommonItemResponse item = detailCommonList.getDetailCommonItemResponse();

        //then
        assertThat(item.getContentId()).isEqualTo(compareItem.getContentId());
        assertThat(item.getContentTypeId()).isEqualTo(compareItem.getContentTypeId());
        assertThat(item.getAddress1()).isEqualTo(compareItem.getAddress1());
        assertThat(item.getTitle()).isEqualTo(compareItem.getTitle());
    }

    @Test
    @DisplayName("소개 정보 조회 API 통신 테스트")
    public void callDetailIntroAPITest() {
        //given
        String areaCode = "1";
        String sigunguCode = "3";
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());
        int page = 1;
        int pageSize = 1;
        String arrangeType = ArrangeType.TITLE.getCode();

        TourAPICommonListResponse tourAPICommonList =
                dataGoKrAPIService.getAreaBasedTourApi(areaCode, sigunguCode, page, pageSize, contentTypeId, arrangeType);
        TourAPICommonItemResponse compareItem = tourAPICommonList.getTourAPICommonItemResponseList().get(0);

        // when
        DetailIntroResponse detailIntroItem = dataGoKrAPIService.getIntroApi(compareItem.getContentId(), contentTypeId);
        SpotItemResponse item = ((SpotIntroResponse) detailIntroItem).getSpotItemResponses();

        // then
        assertThat(item.getContentId()).isEqualTo(compareItem.getContentId());
        assertThat(item.getContentTypeId()).isEqualTo(compareItem.getContentTypeId());
    }

    @Test
    @DisplayName("반복 정보 조회 API 통신 테스트")
    public void callDetailInfoAPITest() {
        //given
        String areaCode = "1";
        String sigunguCode = "3";
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());
        int page = 1;
        int pageSize = 1;
        String arrangeType = ArrangeType.TITLE.getCode();

        TourAPICommonListResponse tourAPICommonList =
                dataGoKrAPIService.getAreaBasedTourApi(areaCode, sigunguCode, page, pageSize, contentTypeId, arrangeType);
        TourAPICommonItemResponse compareItem = tourAPICommonList.getTourAPICommonItemResponseList().get(0);

        //then
        DetailInfoListResponse detailInfoListResponse = dataGoKrAPIService.getInfoApi(compareItem.getContentId(), contentTypeId);
        List<DetailInfoItemResponse> detailItemList = detailInfoListResponse.getDetailInfoItemResponses();

        // when
        for(DetailInfoItemResponse item : detailItemList) {
            assertThat(item.getContentId()).isEqualTo(compareItem.getContentId());
            assertThat(item.getContentTypeId()).isEqualTo(compareItem.getContentTypeId());
        }
    }

    @Test
    @DisplayName("이미지 조회 API 통신 테스트")
    public void callImageAPITest() {
        // given
        String areaCode = "1";
        String sigunguCode = "3";
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());
        int page = 1;
        int pageSize = 10;
        String arrangeType = ArrangeType.TITLE.getCode();

        TourAPICommonListResponse tourAPICommonList =
                dataGoKrAPIService.getAreaBasedTourApi(areaCode, sigunguCode, page, pageSize, contentTypeId, arrangeType);
        TourAPICommonItemResponse compareItem = tourAPICommonList.getTourAPICommonItemResponseList().get(0);

        // then
        DetailImageListResponse detailImageListResponse = dataGoKrAPIService.getImageApi(compareItem.getContentId());
        List<DetailImageItemResponse> detailItemList = detailImageListResponse.getDetailImageItemResponses();

        // when
        for(DetailImageItemResponse item : detailItemList) {
            assertThat(item.getContentId()).isEqualTo(compareItem.getContentId());

            String urlPattern = "^(https?|ftp)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(/[a-zA-Z0-9-_.]*)+\\.(jpg|jpeg|png|gif)$";
            assertThat(item.getOriginImageUrl().matches(urlPattern)).isEqualTo(true);
        }

    }

    @Test
    @DisplayName("초단기 실황 조회 API 통신 테스트")
    public void callUltraSrtNcstAPITest() {
        double mapX = 126.98935225645432;
        double mapY = 37.579871128849334;

        FcstAPICommonListResponse response = dataGoKrAPIService.getNowForecast(mapX, mapY);

        assertThat(response.getFcstAPICommonItemResponseList().size()).isEqualTo(8);
    }
}
