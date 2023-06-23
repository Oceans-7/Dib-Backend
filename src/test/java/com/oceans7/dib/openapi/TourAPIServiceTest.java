package com.oceans7.dib.openapi;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.openapi.dto.response.detail.common.DetailCommonListResponse;
import com.oceans7.dib.openapi.dto.response.simple.AreaCodeList;
import com.oceans7.dib.openapi.dto.response.simple.TourAPICommonListResponse;
import com.oceans7.dib.openapi.service.TourAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TourAPIServiceTest {
    @Autowired
    TourAPIService tourAPIService;

    @Test
    @DisplayName("위치 기반 서비스 API 통신 테스트")
    public void callLocationBasedAPITest() {
        // given
        double mapX = 126.9779692;
        double mapY = 37.566535;
        ContentType contentType = ContentType.TOURIST_SPOT;

        // when
        TourAPICommonListResponse list = tourAPIService.fetchDataFromLocationBasedApi(mapX, mapY, contentType);

        // then
        assertThat(list.getTourAPICommonItemResponseList().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("키워드 검색 서비스 API 통신 테스트")
    public void callSearchKeywordAPITest() {
        // given
        String keyword = "강원";

        // when
        TourAPICommonListResponse list = tourAPIService.fetchDataFromSearchKeywordApi(keyword);

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

        TourAPICommonListResponse tourAPICommonList = tourAPIService.fetchDataFromAreaBasedApi(areaCode, sigunguCode);

        assertThat(tourAPICommonList.getTourAPICommonItemResponseList().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("공통 정보 조회 API 통신 테스트")
    public void callDetailCommonAPITest() {
        //given
        String keyword = "강원";

        TourAPICommonListResponse list = tourAPIService.fetchDataFromSearchKeywordApi(keyword);
        Long contentId = list.getTourAPICommonItemResponseList().get(0).getContentId();
        int contentTypeId = list.getTourAPICommonItemResponseList().get(0).getContentTypeId();
        ContentType contentType = ContentType.getContentTypeByCode(contentTypeId);

        DetailCommonListResponse detailCommonList = tourAPIService.fetchDataFromCommonApi(contentId, contentType);

        assertThat(detailCommonList.getDetailCommonItemResponse().getContentId()).isEqualTo(contentId);
        assertThat(detailCommonList.getDetailCommonItemResponse().getContentTypeId()).isEqualTo(contentTypeId);
    }

    @Test
    @DisplayName("소개 정보 조회 API 통신 테스트")
    public void callDetailIntroAPITest() {

    }

    @Test
    @DisplayName("반복 정보 조회 API 통신 테스트")
    public void callDetailInfoAPITest() {

    }

    @Test
    @DisplayName("이미지 조회 API 통신 테스트")
    public void callImageAPITest() {

    }
}
