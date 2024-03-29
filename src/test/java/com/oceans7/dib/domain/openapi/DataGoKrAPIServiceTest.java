package com.oceans7.dib.domain.openapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.global.MockRequest;
import com.oceans7.dib.global.MockResponse;
import com.oceans7.dib.global.ResponseWrapper;
import com.oceans7.dib.global.api.http.DataGoKrApi;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroResponse.*;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroItemResponse.*;
import com.oceans7.dib.global.api.response.tourapi.list.AreaCodeItem;
import com.oceans7.dib.global.api.response.tourapi.list.AreaCodeList;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonItemResponse;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonListResponse;
import com.oceans7.dib.global.api.service.DataGoKrAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataGoKrAPIServiceTest {
    @Autowired
    DataGoKrAPIService dataGoKrAPIService;

    @MockBean
    private DataGoKrApi dataGoKrApi;

    @Value("${open-api.data-go-kr.service-decode-key}")
    private String serviceKey;

    @Value("${open-api.data-go-kr.data-type}")
    private String dataType;

    @Value("${open-api.data-go-kr.mobile-os}")
    private String mobileOS;

    @Value("${open-api.data-go-kr.mobile-app}")
    private String mobileApp;

    private ObjectMapper objectMapper;

    @BeforeEach
    void before() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("위치 기반 서비스 API 통신 테스트")
    public void callLocationBasedAPITest() throws JsonProcessingException {
        // given
        int page = 1;
        int pageSize = 1;
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());
        ArrangeType arrangeType = ArrangeType.E;

        ResponseWrapper locationBasedAPIRes = MockResponse.testLocationBasedRes();
        String apiResponse = objectMapper.writeValueAsString(locationBasedAPIRes);
        when(dataGoKrApi.getLocationBasedTourInfo(serviceKey, mobileOS, mobileApp, dataType,
                MockRequest.X, MockRequest.Y, MockRequest.RADIUS,
                page, pageSize, contentTypeId, arrangeType.name()))
        .thenReturn(apiResponse);

        // when
        TourAPICommonListResponse list = dataGoKrAPIService.getLocationBasedTourApi(MockRequest.X, MockRequest.Y,
                page, pageSize, contentTypeId, arrangeType.name());
        TourAPICommonItemResponse item = list.getTourAPICommonItemResponseList().get(0);

        // then
        assertThat(list.getPage()).isEqualTo(page);
        assertThat(list.getPageSize()).isEqualTo(pageSize);

        assertThat(item.getDistance()).isEqualTo(1000.1711716167842);
        assertThat(item.getContentTypeId()).isEqualTo(ContentType.TOURIST_SPOT.getCode());

        assertTourAPI(item);
    }

    @Test
    @DisplayName("키워드 검색 서비스 API 통신 테스트")
    public void callSearchKeywordAPITest() throws JsonProcessingException {
        // given
        String keyword = "뷰티플레이";
        int page = 1;
        int pageSize = 1;

        ResponseWrapper keywordBasedAPIRes = MockResponse.testKeywordBasedRes();
        String apiResponse = objectMapper.writeValueAsString(keywordBasedAPIRes);
        when(dataGoKrApi.getSearchKeywordTourInfo(serviceKey, mobileOS, mobileApp, dataType,
                keyword, page, pageSize)).thenReturn(apiResponse);

        // when
        TourAPICommonListResponse list = dataGoKrAPIService.getSearchKeywordTourApi(keyword, page, pageSize);
        TourAPICommonItemResponse item = list.getTourAPICommonItemResponseList().get(0);

        // then
        assertThat(list.getPage()).isEqualTo(page);
        assertThat(list.getPageSize()).isEqualTo(pageSize);

        assertThat(item.getTitle()).isEqualTo(keyword);
        assertTourAPI(item);
    }


    @Test
    @DisplayName("지역 기반 관광정보 조회 API 통신 테스트")
    public void callAreaBasedAPITest() throws JsonProcessingException {
        // given : '서울 중구'
        String areaCode = "1";
        String sigunguCode = "24";
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());
        String arrangeType = "";
        int page = 1;
        int pageSize = 1;

        ResponseWrapper areaBasedAPIRes = MockResponse.testAreaBasedRes();
        String apiResponse = objectMapper.writeValueAsString(areaBasedAPIRes);
        when(dataGoKrApi.getAreaBasedTourInfo(serviceKey, mobileOS, mobileApp, dataType,
                areaCode, sigunguCode, page, pageSize,
                contentTypeId, arrangeType))
                .thenReturn(apiResponse);

        // when
        TourAPICommonListResponse list = dataGoKrAPIService.getAreaBasedTourApi(areaCode, sigunguCode, page, pageSize, contentTypeId, arrangeType);
        TourAPICommonItemResponse item = list.getTourAPICommonItemResponseList().get(0);

        // then
        assertThat(item.getAddress().contains("서울") && item.getAddress().contains("중구")).isEqualTo(true);
        assertTourAPI(item);
    }

    public void assertTourAPI(TourAPICommonItemResponse item) {
        String urlPattern = "^(https?|ftp)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(/[a-zA-Z0-9-_.]*)+\\.(jpg|jpeg|png|gif|bmp)$";

        assertThat(item.getContentId()).isEqualTo(2946230);
        assertThat(item.getTitle()).isEqualTo("뷰티플레이");
        assertThat(item.getTel()).isEqualTo("");
        assertThat(item.getThumbnail().matches(urlPattern)).isEqualTo(true);
        assertThat(item.getAddress()).isEqualTo("서울특별시 중구 명동1가 1-3 YWCA연합회 ");
        assertThat(item.getMapX()).isEqualTo(MockRequest.X);
        assertThat(item.getMapY()).isEqualTo(MockRequest.Y);
        assertThat(item.getSigunguCode()).isEqualTo("24");
        assertThat(item.getAreaCode()).isEqualTo("1");
    }

    @Test
    @DisplayName("지역 코드 조회 API 통신 테스트")
    public void callAreaCodeAPITest() throws JsonProcessingException {
        // given
        String seoulCode = "1";

        ResponseWrapper areaCodeAPIRes = MockResponse.testAreaCodeRes();
        String apiResponse = objectMapper.writeValueAsString(areaCodeAPIRes);
        when(dataGoKrApi.getAreaCode(serviceKey, mobileOS, mobileApp, dataType,
                MockRequest.MAX_AREA_CODE_SIZE, "")).thenReturn(apiResponse);

        ResponseWrapper sigunguCodeAPIRes = MockResponse.testSigunguCodeRes();
        apiResponse = objectMapper.writeValueAsString(sigunguCodeAPIRes);
        when(dataGoKrApi.getAreaCode(serviceKey, mobileOS, mobileApp, dataType,
                MockRequest.MAX_AREA_CODE_SIZE, seoulCode)).thenReturn(apiResponse);

        // when
        AreaCodeList areaList = dataGoKrAPIService.getAreaCodeApi("");
        AreaCodeList sigunguList = dataGoKrAPIService.getAreaCodeApi(seoulCode);

        List<AreaCodeItem> areaItems = areaList.getAreaCodeItems();
        List<AreaCodeItem> sigunguItems = sigunguList.getAreaCodeItems();

        // then
        assertThat(areaItems.get(0).getName()).isEqualTo("서울");
        assertThat(areaItems.get(0).getCode()).isEqualTo("1");
        assertThat(areaItems.get(1).getName()).isEqualTo("인천");
        assertThat(areaItems.get(1).getCode()).isEqualTo("2");
        assertThat(areaItems.get(2).getName()).isEqualTo("대전");
        assertThat(areaItems.get(2).getCode()).isEqualTo("3");

        assertThat(sigunguItems.get(0).getName()).isEqualTo("강남구");
        assertThat(sigunguItems.get(0).getCode()).isEqualTo("1");
        assertThat(sigunguItems.get(1).getName()).isEqualTo("강동구");
        assertThat(sigunguItems.get(1).getCode()).isEqualTo("2");
        assertThat(sigunguItems.get(2).getName()).isEqualTo("강북구");
        assertThat(sigunguItems.get(2).getCode()).isEqualTo("3");
    }

    @Test
    @DisplayName("공통 정보 조회 API 통신 테스트")
    public void callDetailCommonAPITest() throws JsonProcessingException {
        //given
        Long contentId = (long) 2946230;
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());

        ResponseWrapper detailCommonAPIRes = MockResponse.testDetailCommonRes();
        String apiResponse = objectMapper.writeValueAsString(detailCommonAPIRes);
        when(dataGoKrApi.getTourCommonInfo(serviceKey, mobileOS, mobileApp, dataType, contentId, contentTypeId,
                MockRequest.YES_OPTION, MockRequest.YES_OPTION, MockRequest.YES_OPTION, MockRequest.YES_OPTION, MockRequest.YES_OPTION, MockRequest.YES_OPTION))
                .thenReturn(apiResponse);

        // when
        DetailCommonListResponse list = dataGoKrAPIService.getCommonApi(contentId, contentTypeId);
        DetailCommonItemResponse item = list.getDetailCommonItemResponse();

        //then
        assertThat(item.getHomepageUrl()).isEqualTo("www.beautyplay.kr");
        assertThat(item.getOverview()).isEqualTo("");
        assertTourAPI(item);
    }

    @Test
    @DisplayName("소개 정보 조회 API 통신 테스트")
    public void callDetailIntroAPITest() throws JsonProcessingException {
        //given
        Long contentId = (long) 2946230;
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());

        ResponseWrapper detailIntroAPIRes = MockResponse.testDetailIntroRes();
        String apiResponse = objectMapper.writeValueAsString(detailIntroAPIRes);
        when(dataGoKrApi.getTourIntroInfo(serviceKey, mobileOS, mobileApp, dataType,
                contentId, contentTypeId))
                .thenReturn(apiResponse);

        // when
        DetailIntroResponse detailIntroItem = dataGoKrAPIService.getIntroApi(contentId, contentTypeId);
        SpotItemResponse item = ((SpotIntroResponse) detailIntroItem).getSpotItemResponse();

        // then
        assertThat(item.getInfoCenter()).isEqualTo("070-4070-9675");
        assertThat(item.getCheckBabyCarriage()).isEqualTo("");
        assertThat(item.getCheckCreditCard()).isEqualTo("");
        assertThat(item.getCheckPet()).isEqualTo("");
        assertThat(item.getRestDate()).isEqualTo("일요일");
        assertThat(item.getUseTime()).isEqualTo("10:00~19:00(뷰티 체험은 18:00까지)");
    }

    @Test
    @DisplayName("반복 정보 조회 API 통신 테스트")
    public void callDetailInfoAPITest() throws JsonProcessingException {
        //given
        Long contentId = (long) 2946230;
        String contentTypeId = String.valueOf(ContentType.TOURIST_SPOT.getCode());

        ResponseWrapper detailInfoAPIRes = MockResponse.testDetailInfoRes();
        String apiResponse = objectMapper.writeValueAsString(detailInfoAPIRes);
        when(dataGoKrApi.getTourInfo(serviceKey, mobileOS, mobileApp, dataType,
                contentId, contentTypeId))
                .thenReturn(apiResponse);

        // when
        DetailInfoListResponse list = dataGoKrAPIService.getInfoApi(contentId, contentTypeId);
        DetailInfoItemResponse item = list.getDetailInfoItemResponses().get(0);

        // then
        assertThat(item.getContentId()).isEqualTo(contentId);
        assertThat(item.getContentTypeId()).isEqualTo(ContentType.TOURIST_SPOT.getCode());
        assertThat(item.getInfoName()).isEqualTo("화장실");
        assertThat(item.getInfoText()).isEqualTo("있음");
    }

    @Test
    @DisplayName("이미지 조회 API 통신 테스트")
    public void callImageAPITest() throws JsonProcessingException {
        //given
        Long contentId = (long) 2946230;

        ResponseWrapper detailImageAPIRes = MockResponse.testDetailImageRes();
        String apiResponse = objectMapper.writeValueAsString(detailImageAPIRes);
        when(dataGoKrApi.getTourImageInfo(serviceKey, mobileOS, mobileApp, dataType,
                contentId, MockRequest.YES_OPTION))
                .thenReturn(apiResponse);

        // when
        DetailImageListResponse list = dataGoKrAPIService.getImageApi(contentId);
        List<DetailImageItemResponse> itemList = list.getDetailImageItemResponses();

        // then
        for(DetailImageItemResponse item : itemList) {
            assertThat(item.getContentId()).isEqualTo(contentId);

            String urlPattern = "^(https?|ftp)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(/[a-zA-Z0-9-_.]*)+\\.(jpg|jpeg|png|gif|bmp)$";
            assertThat(item.getOriginImageUrl().matches(urlPattern)).isEqualTo(true);
        }

    }
}
