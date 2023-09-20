package com.oceans7.dib.domain.place.controller;

import com.oceans7.dib.domain.place.dto.PlaceFilterOptions;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto;
import com.oceans7.dib.domain.place.dto.response.SimpleAreaResponseDto;
import com.oceans7.dib.domain.place.dto.response.SimplePlaceInformationDto;
import com.oceans7.dib.domain.place.service.PlaceService;
import com.oceans7.dib.global.MockRequest;
import com.oceans7.dib.global.MockResponse;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaceController.class)
public class PlaceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PlaceService placeService;

    @Test
    @DisplayName("위치 기반 관광 정보 조회 테스트")
    @WithMockUser("user1")
    public void getPlaceBasedLocationTest() throws Exception {
        //given
        GetPlaceRequestDto placeReq = MockRequest.testPlaceReq();
        PlaceFilterOptions options = MockRequest.testPlaceFilterOptionReq(placeReq);

        when(placeService.getPlace(placeReq, options))
                .thenReturn(MockResponse.testGetPlaceRes());

        // when
        ResultActions result = mvc.perform(get("/place")
                .param("mapX", String.valueOf(placeReq.getMapX()))
                .param("mapY", String.valueOf(placeReq.getMapY()))
                .param("contentType", String.valueOf(placeReq.getContentType()))
                .param("page", String.valueOf(placeReq.getPage()))
                .param("pageSize", String.valueOf(placeReq.getPageSize())));
        SimplePlaceInformationDto testGetPlaceInfoRes = MockResponse.testGetPlaceRes().getPlaces().get(0);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(MockResponse.testGetPlaceRes().getCount()))
                .andExpect(jsonPath("$.data.page").value(MockResponse.testGetPlaceRes().getPage()))
                .andExpect(jsonPath("$.data.pageSize").value(MockResponse.testGetPlaceRes().getPageSize()))
                .andExpect(jsonPath("$.data.arrangeType").value(MockResponse.testGetPlaceRes().getArrangeType()))
                .andExpect(jsonPath("$.data.places[0].title").value(testGetPlaceInfoRes.getTitle()))
                .andExpect(jsonPath("$.data.places[0].address").value(testGetPlaceInfoRes.getAddress()))
                .andExpect(jsonPath("$.data.places[0].contentId").value(testGetPlaceInfoRes.getContentId()))
                .andExpect(jsonPath("$.data.places[0].contentType").value(String.valueOf(testGetPlaceInfoRes.getContentType())))
                .andExpect(jsonPath("$.data.places[0].distance").value(testGetPlaceInfoRes.getDistance()))
                .andExpect(jsonPath("$.data.places[0].firstImage").value(testGetPlaceInfoRes.getFirstImage()))
                .andExpect(jsonPath("$.data.places[0].tel").value(testGetPlaceInfoRes.getTel()));
    }

    @Test
    @DisplayName("지역 기반 관광 정보 조회 테스트")
    @WithMockUser("user1")
    public void getPlaceBasedAreaTest() throws Exception {
        //given
        GetPlaceRequestDto placeWithAreaReq = MockRequest.testPlaceWithAreaReq();
        PlaceFilterOptions filterOption = MockRequest.testPlaceFilterOptionReq(placeWithAreaReq);

        when(placeService.getPlace(placeWithAreaReq, filterOption))
                .thenReturn(MockResponse.testGetPlaceBasedAreaRes());

        // when
        ResultActions result = mvc.perform(get("/place")
                .param("mapX", String.valueOf(placeWithAreaReq.getMapX()))
                .param("mapY", String.valueOf(placeWithAreaReq.getMapY()))
                .param("area", placeWithAreaReq.getArea())
                .param("sigungu", placeWithAreaReq.getSigungu())
                .param("contentType", String.valueOf(placeWithAreaReq.getContentType()))
                .param("page", String.valueOf(placeWithAreaReq.getPage()))
                .param("pageSize", String.valueOf(placeWithAreaReq.getPageSize())));
        SimplePlaceInformationDto testGetPlaceInfoRes = MockResponse.testGetPlaceRes().getPlaces().get(0);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(MockResponse.testGetPlaceBasedAreaRes().getCount()))
                .andExpect(jsonPath("$.data.page").value(MockResponse.testGetPlaceBasedAreaRes().getPage()))
                .andExpect(jsonPath("$.data.pageSize").value(MockResponse.testGetPlaceBasedAreaRes().getPageSize()))
                .andExpect(jsonPath("$.data.arrangeType").value(MockResponse.testGetPlaceBasedAreaRes().getArrangeType()))
                .andExpect(jsonPath("$.data.places[0].title").value(testGetPlaceInfoRes.getTitle()))
                .andExpect(jsonPath("$.data.places[0].address").value(testGetPlaceInfoRes.getAddress()))
                .andExpect(jsonPath("$.data.places[0].contentId").value(testGetPlaceInfoRes.getContentId()))
                .andExpect(jsonPath("$.data.places[0].contentType").value(String.valueOf(testGetPlaceInfoRes.getContentType())))
                .andExpect(jsonPath("$.data.places[0].distance").value(testGetPlaceInfoRes.getDistance()))
                .andExpect(jsonPath("$.data.places[0].firstImage").value(testGetPlaceInfoRes.getFirstImage()))
                .andExpect(jsonPath("$.data.places[0].tel").value(testGetPlaceInfoRes.getTel()));
    }

    @Test
    @DisplayName("관광 정보 조회 필터링, 정렬 테스트")
    @WithMockUser("user1")
    public void getPlaceBasedLocationWithContentTypeAndSortingTest() throws Exception {
        //given
        GetPlaceRequestDto placeWithSortingReq = MockRequest.testPlaceWithSortingReq();
        PlaceFilterOptions options = MockRequest.testPlaceFilterOptionReq(placeWithSortingReq);

        when(placeService.getPlace(placeWithSortingReq, options))
                .thenReturn(MockResponse.testGetPlaceRes());

        // when
        ResultActions result = mvc.perform(get("/place")
                .param("mapX", String.valueOf(placeWithSortingReq.getMapX()))
                .param("mapY", String.valueOf(placeWithSortingReq.getMapY()))
                .param("contentType", String.valueOf(placeWithSortingReq.getContentType()))
                .param("arrangeType",String.valueOf(placeWithSortingReq.getArrangeType()))
                .param("page", String.valueOf(placeWithSortingReq.getPage()))
                .param("pageSize", String.valueOf(placeWithSortingReq.getPageSize())));
        SimplePlaceInformationDto testPlaceInfoResAtIndex0 = MockResponse.testGetPlaceRes().getPlaces().get(0);
        SimplePlaceInformationDto testPlaceInfoResAtIndex1 = MockResponse.testGetPlaceRes().getPlaces().get(1);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(MockResponse.testGetPlaceRes().getCount()))
                .andExpect(jsonPath("$.data.page").value(MockResponse.testGetPlaceRes().getPage()))
                .andExpect(jsonPath("$.data.pageSize").value(MockResponse.testGetPlaceRes().getPageSize()))
                .andExpect(jsonPath("$.data.arrangeType").value(MockResponse.testGetPlaceRes().getArrangeType()))
                .andExpect(jsonPath("$.data.places[0].title").value(testPlaceInfoResAtIndex0.getTitle()))
                .andExpect(jsonPath("$.data.places[0].contentType").value(String.valueOf(testPlaceInfoResAtIndex0.getContentType())))
                .andExpect(jsonPath("$.data.places[1].title").value(testPlaceInfoResAtIndex1.getTitle()))
                .andExpect(jsonPath("$.data.places[1].contentType").value(String.valueOf(testPlaceInfoResAtIndex1.getContentType())))
        ;

        String jsonResponse = result.andReturn().getResponse().getContentAsString();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray placesArray = jsonObject.getJSONObject("data").getJSONArray("places");

        double distanceAtIndex0 = placesArray.getJSONObject(0).getDouble("distance");
        double distanceAtIndex1 = placesArray.getJSONObject(1).getDouble("distance");

        assertTrue(distanceAtIndex0 < distanceAtIndex1);
    }

    @Test
    @DisplayName("[exception] 유효하지 않은 좌표 테스트")
    @WithMockUser("user1")
    public void getPlaceInvalidXYThrowsExceptionTest() throws Exception {
        //given
        GetPlaceRequestDto placeXYExceptionReq = MockRequest.testPlaceXYExceptionReq();
        PlaceFilterOptions options = MockRequest.testPlaceFilterOptionReq(placeXYExceptionReq);

        when(placeService.getPlace(placeXYExceptionReq, options))
                .thenThrow(new ApplicationException(ErrorCode.NOT_FOUND_ITEM_EXCEPTION));

        // when
        ResultActions result = mvc.perform(get("/place")
                .param("mapX", String.valueOf(placeXYExceptionReq.getMapX()))
                .param("mapY", String.valueOf(placeXYExceptionReq.getMapY()))
                .param("page", String.valueOf(placeXYExceptionReq.getPage()))
                .param("pageSize", String.valueOf(placeXYExceptionReq.getPageSize())));

        // then
        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND_ITEM_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND_ITEM_EXCEPTION.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("[exception] 유효하지 않은 콘텐츠 타입 테스트")
    @WithMockUser("user1")
    public void getPlaceInvalidContentTypeThrowsExceptionTest() throws Exception {
        //given
        String contentType = "Invalid Content Type!!";

        // when
        ResultActions result = mvc.perform(get("/place")
                .param("mapX", String.valueOf(MockRequest.X))
                .param("mapY", String.valueOf(MockRequest.Y))
                .param("contentType", contentType)
                .param("page", "1")
                .param("pageSize", "1"));

        //then
        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_VALUE_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value("contentType은(는) 올바르지 않은 요청 값입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("[exception] 유효하지 않은 정렬 타입 테스트")
    @WithMockUser("user1")
    public void getPlaceInvalidArrangeTypeThrowsExceptionTest() throws Exception {
        //given
        String arrangeType = "Invalid Arrange Type!!";

        // when
        ResultActions result = mvc.perform(get("/place")
                .param("mapX", String.valueOf(MockRequest.X))
                .param("mapY", String.valueOf(MockRequest.Y))
                .param("arrangeType", arrangeType)
                .param("page", "1")
                .param("pageSize", "1"));

        //then
        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_VALUE_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value("arrangeType은(는) 올바르지 않은 요청 값입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("[exception] 유효하지 않은 지역 테스트")
    @WithMockUser("user1")
    public void getPlaceInvalidAreaThrowsExceptionTest() throws Exception {
        //given
        GetPlaceRequestDto placeAreaExceptionReq = MockRequest.testPlaceAreaExceptionReq();
        PlaceFilterOptions options = MockRequest.testPlaceFilterOptionReq(placeAreaExceptionReq);


        when(placeService.getPlace(placeAreaExceptionReq, options))
                .thenThrow(new ApplicationException(ErrorCode.NOT_FOUND_AREA_NAME));

        // when
        ResultActions result = mvc.perform(get("/place")
                .param("mapX", String.valueOf(placeAreaExceptionReq.getMapX()))
                .param("mapY", String.valueOf(placeAreaExceptionReq.getMapY()))
                .param("area", placeAreaExceptionReq.getArea())
                .param("page", String.valueOf(placeAreaExceptionReq.getPage()))
                .param("pageSize", String.valueOf(placeAreaExceptionReq.getPageSize())));

        //then
        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND_AREA_NAME.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND_AREA_NAME.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("키워드 검색 테스트")
    @WithMockUser("user1")
    public void searchPlaceTest() throws Exception {
        //given
        SearchPlaceRequestDto searchReq = MockRequest.testSearchReq();
        when(placeService.searchKeyword(searchReq))
                .thenReturn(MockResponse.testSearchPlaceBasedKeywordRes());

        // when
        ResultActions result = mvc.perform(get("/place/search")
                .param("mapX", String.valueOf(searchReq.getMapX()))
                .param("mapY", String.valueOf(searchReq.getMapY()))
                .param("keyword", searchReq.getKeyword())
                .param("page", String.valueOf(searchReq.getPage()))
                .param("pageSize", String.valueOf(searchReq.getPageSize())));
        SimplePlaceInformationDto testSearchPlaceInfoRes = MockResponse.testSearchPlaceBasedKeywordRes().getPlaces().get(0);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.keyword").value(MockResponse.testSearchPlaceBasedKeywordRes().getKeyword()))
                .andExpect(jsonPath("$.data.count").value(MockResponse.testSearchPlaceBasedKeywordRes().getCount()))
                .andExpect(jsonPath("$.data.page").value(MockResponse.testSearchPlaceBasedKeywordRes().getPage()))
                .andExpect(jsonPath("$.data.pageSize").value(MockResponse.testSearchPlaceBasedKeywordRes().getPageSize()))
                .andExpect(jsonPath("$.data.areaSearch").value(MockResponse.testSearchPlaceBasedKeywordRes().isAreaSearch()))
                .andExpect(jsonPath("$.data.places[0].title").value(testSearchPlaceInfoRes.getTitle()))
                .andExpect(jsonPath("$.data.places[0].address").value(testSearchPlaceInfoRes.getAddress()))
                .andExpect(jsonPath("$.data.places[0].contentId").value(testSearchPlaceInfoRes.getContentId()))
                .andExpect(jsonPath("$.data.places[0].contentType").value(String.valueOf(testSearchPlaceInfoRes.getContentType())))
                .andExpect(jsonPath("$.data.places[0].distance").value(testSearchPlaceInfoRes.getDistance()))
                .andExpect(jsonPath("$.data.places[0].firstImage").value(testSearchPlaceInfoRes.getFirstImage()))
                .andExpect(jsonPath("$.data.places[0].tel").value(testSearchPlaceInfoRes.getTel()));
    }

    @Test
    @DisplayName("지역 검색 테스트")
    @WithMockUser("user1")
    public void searchAreaTest() throws Exception {
        //given
        SearchPlaceRequestDto searchAreaReq = MockRequest.testSearchAreaReq();
        when(placeService.searchKeyword(searchAreaReq))
                .thenReturn(MockResponse.testSearchPlaceBasedAreaRes());

        // when
        ResultActions result = mvc.perform(get("/place/search")
                .param("mapX", String.valueOf(searchAreaReq.getMapX()))
                .param("mapY", String.valueOf(searchAreaReq.getMapY()))
                .param("keyword", searchAreaReq.getKeyword())
                .param("page", String.valueOf(searchAreaReq.getPage()))
                .param("pageSize", String.valueOf(searchAreaReq.getPageSize())));
        SimpleAreaResponseDto testSearchAreaInfoRes = MockResponse.testSearchPlaceBasedAreaRes().getAreas().get(0);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.keyword").value(MockResponse.testSearchPlaceBasedAreaRes().getKeyword()))
                .andExpect(jsonPath("$.data.count").value(MockResponse.testSearchPlaceBasedAreaRes().getCount()))
                .andExpect(jsonPath("$.data.page").value(MockResponse.testSearchPlaceBasedAreaRes().getPage()))
                .andExpect(jsonPath("$.data.pageSize").value(MockResponse.testSearchPlaceBasedAreaRes().getPageSize()))
                .andExpect(jsonPath("$.data.areaSearch").value(MockResponse.testSearchPlaceBasedAreaRes().isAreaSearch()))
                .andExpect(jsonPath("$.data.areas[0].address").value(testSearchAreaInfoRes.getAddress()))
                .andExpect(jsonPath("$.data.areas[0].areaName").value(testSearchAreaInfoRes.getAreaName()))
                .andExpect(jsonPath("$.data.areas[0].sigunguName").value(testSearchAreaInfoRes.getSigunguName()))
                .andExpect(jsonPath("$.data.areas[0].distance").value(String.valueOf(testSearchAreaInfoRes.getDistance())))
                .andExpect(jsonPath("$.data.areas[0].mapX").value(String.valueOf(testSearchAreaInfoRes.getMapX())))
                .andExpect(jsonPath("$.data.areas[0].mapY").value(String.valueOf(testSearchAreaInfoRes.getMapY())));
    }

    @Test
    @DisplayName("[exception] 검색어 결과 없음 테스트")
    @WithMockUser("user1")
    public void searchPlaceNotFoundItemExceptionTest() throws Exception {
        //given
        SearchPlaceRequestDto searchNotFoundExceptionReq = MockRequest.testSearchNotFoundExceptionReq();
        when(placeService.searchKeyword(searchNotFoundExceptionReq))
                .thenThrow(new ApplicationException(ErrorCode.NOT_FOUND_ITEM_EXCEPTION));

        // when
        ResultActions result = mvc.perform(get("/place/search")
                .param("mapX", String.valueOf(searchNotFoundExceptionReq.getMapX()))
                .param("mapY", String.valueOf(searchNotFoundExceptionReq.getMapY()))
                .param("keyword", searchNotFoundExceptionReq.getKeyword())
                .param("page", String.valueOf(searchNotFoundExceptionReq.getPage()))
                .param("pageSize", String.valueOf(searchNotFoundExceptionReq.getPageSize())));

        //then
        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND_ITEM_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND_ITEM_EXCEPTION.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("관광 정보 상세 조회 테스트")
    @WithMockUser("user1")
    public void getPlaceDetail() throws Exception {
        //given
        GetPlaceDetailRequestDto placeDetailReq = MockRequest.testPlaceDetailReq();
        when(placeService.getPlaceDetail(placeDetailReq))
                .thenReturn(MockResponse.testGetDetailPlaceRes());

        // when
        ResultActions result = mvc.perform(get("/place/detail")
                .param("contentId", String.valueOf(placeDetailReq.getContentId()))
                .param("contentType", String.valueOf(placeDetailReq.getContentType())));
        List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = MockResponse.testGetDetailPlaceRes().getFacilityInfo();
        List<String> image = MockResponse.testGetDetailPlaceRes().getImages();

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.contentId").value(MockResponse.testGetDetailPlaceRes().getContentId()))
                .andExpect(jsonPath("$.data.contentType").value(String.valueOf(MockResponse.testGetDetailPlaceRes().getContentType())))
                .andExpect(jsonPath("$.data.title").value(MockResponse.testGetDetailPlaceRes().getTitle()))
                .andExpect(jsonPath("$.data.address").value(MockResponse.testGetDetailPlaceRes().getAddress()))
                .andExpect(jsonPath("$.data.mapX").value(MockResponse.testGetDetailPlaceRes().getMapX()))
                .andExpect(jsonPath("$.data.mapY").value(MockResponse.testGetDetailPlaceRes().getMapY()))
                .andExpect(jsonPath("$.data.introduce").value(MockResponse.testGetDetailPlaceRes().getIntroduce()))
                .andExpect(jsonPath("$.data.homepageUrl").value(MockResponse.testGetDetailPlaceRes().getHomepageUrl()))
                .andExpect(jsonPath("$.data.useTime").value(MockResponse.testGetDetailPlaceRes().getUseTime()))
                .andExpect(jsonPath("$.data.tel").value(MockResponse.testGetDetailPlaceRes().getTel()))
                .andExpect(jsonPath("$.data.facilityInfo[0].type").value(String.valueOf(facilityInfo.get(0).getType())))
                .andExpect(jsonPath("$.data.facilityInfo[0].availability").value(facilityInfo.get(0).isAvailability()))
                .andExpect(jsonPath("$.data.facilityInfo[1].type").value(String.valueOf(facilityInfo.get(1).getType())))
                .andExpect(jsonPath("$.data.facilityInfo[1].availability").value(facilityInfo.get(1).isAvailability()))
                .andExpect(jsonPath("$.data.facilityInfo[2].type").value(String.valueOf(facilityInfo.get(2).getType())))
                .andExpect(jsonPath("$.data.facilityInfo[2].availability").value(facilityInfo.get(2).isAvailability()))
                .andExpect(jsonPath("$.data.facilityInfo[3].type").value(String.valueOf(facilityInfo.get(3).getType())))
                .andExpect(jsonPath("$.data.facilityInfo[3].availability").value(facilityInfo.get(3).isAvailability()))
                .andExpect(jsonPath("$.data.images[0]").value(image.get(0)))
                .andExpect(jsonPath("$.data.images[1]").value(image.get(1)))
                .andExpect(jsonPath("$.data.images[2]").value(image.get(2)))
                .andExpect(jsonPath("$.data.images[3]").value(image.get(3)));
    }
}