package com.oceans7.dib.domain.custom_content.controller;

import com.oceans7.dib.domain.custom_content.dto.response.ContentResponseDto;
import com.oceans7.dib.domain.custom_content.dto.response.detail.*;
import com.oceans7.dib.domain.custom_content.entity.CustomContent;
import com.oceans7.dib.domain.custom_content.repository.CustomContentRepository;
import com.oceans7.dib.domain.custom_content.service.CustomContentService;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomContentController.class)
public class CustomContentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomContentService customContentService;

    @MockBean
    private CustomContentRepository customContentRepository;

    private CustomContent customContent;

    @BeforeEach
    public void before() {
        customContent = makeCustomContent();
    }

    private CustomContent makeCustomContent() {
        CustomContent customContent = MockEntity.testCustomContent();

        ReflectionTestUtils.setField(customContent, "customContentId", 1L);

        when(customContentRepository.save(customContent)).thenReturn(customContent);
        return customContentRepository.save(customContent);
    }

    @Test
    @DisplayName("자체 콘텐츠 리스트 조회")
    @WithMockUser()
    public void getAllCustomContent() throws Exception {
        // given
        List<ContentResponseDto> mockResponse = MockResponse.testCustomContentRes(customContent);
        when(customContentService.getAllCustomContent())
                .thenReturn(mockResponse);

        // when
        ResultActions result = mvc.perform(get("/content"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].customContentId").value(mockResponse.get(0).getCustomContentId()))
                .andExpect(jsonPath("$.data[0].firstImage").value(mockResponse.get(0).getFirstImage()))
                .andExpect(jsonPath("$.data[0].title").value(mockResponse.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].subTitle").value(mockResponse.get(0).getSubTitle()));
    }

    @Test
    @DisplayName("자체 콘텐츠 상세 조회")
    @WithMockUser()
    public void getCustomContentDetail() throws Exception {
        // given
        DetailContentResponseDto mockResponse = MockResponse.testDetailCustomContentRes();
        when(customContentService.getDetailCustomContent(customContent.getCustomContentId()))
                .thenReturn(mockResponse);

        // when
        ResultActions result = mvc.perform(get("/content/" + customContent.getCustomContentId()));

        // then
        TitleSection mockTitleSection = mockResponse.getContent().getTitleSection();
        RegionSection mockRegionSection = mockResponse.getContent().getRegionSection();
        DivingPointSection mockDivingPointSection = mockResponse.getContent().getDivingPointSection();
        DivingShopSection mockDivingShopSection = mockResponse.getContent().getDivingShopSection();
        RestaurantSection mockRestaurantSection = mockResponse.getContent().getRestaurantSection();

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.customContentId").value(mockResponse.getCustomContentId()))
                // 제목 섹션
                .andExpect(jsonPath("$.data.content.titleSection.thumbnailUrl").value(mockTitleSection.getThumbnailUrl()))
                .andExpect(jsonPath("$.data.content.titleSection.title").value(mockTitleSection.getTitle()))
                .andExpect(jsonPath("$.data.content.titleSection.subTitle").value(mockTitleSection.getSubTitle()))
                .andExpect(jsonPath("$.data.content.titleSection.region").value(mockTitleSection.getRegion()))
                .andExpect(jsonPath("$.data.content.titleSection.keyword").value(mockTitleSection.getKeyword()))
                // 지역 소개 섹션
                .andExpect(jsonPath("$.data.content.regionSection.title").value(mockRegionSection.getTitle()))
                .andExpect(jsonPath("$.data.content.regionSection.imageTopContent").value(mockRegionSection.getImageTopContent()))
                .andExpect(jsonPath("$.data.content.regionSection.imageUrl").value(mockRegionSection.getImageUrl()))
                .andExpect(jsonPath("$.data.content.regionSection.imageBottomContent").value(mockRegionSection.getImageBottomContent()))
                .andExpect(jsonPath("$.data.content.regionSection.keyword").value(mockRegionSection.getKeyword()))
                // 다이빙 포인트 소개 섹션
                .andExpect(jsonPath("$.data.content.divingPointSection.title").value(mockDivingPointSection.getTitle()))
                .andExpect(jsonPath("$.data.content.divingPointSection.imageUrl").value(mockDivingPointSection.getImageUrl()))
                .andExpect(jsonPath("$.data.content.divingPointSection.subTitle").value(mockDivingPointSection.getSubTitle()))
                .andExpect(jsonPath("$.data.content.divingPointSection.content").value(mockDivingPointSection.getContent()))
                .andExpect(jsonPath("$.data.content.divingPointSection.keyword").value(mockDivingPointSection.getKeyword()))
                // 다이빙 업체 소개 섹션
                .andExpect(jsonPath("$.data.content.divingShopSection.title").value(mockDivingShopSection.getTitle()))
                .andExpect(jsonPath("$.data.content.divingShopSection.keyword").value(mockDivingShopSection.getKeyword()))
                // 다이빙 업체 소개 섹션 > 1번 다이빙 업체 정보
                .andExpect(jsonPath("$.data.content.divingShopSection.firstShopInfo.title").value(mockDivingShopSection.getFirstShopInfo().getTitle()))
                .andExpect(jsonPath("$.data.content.divingShopSection.firstShopInfo.imageUrl").value(mockDivingShopSection.getFirstShopInfo().getImageUrl()))
                .andExpect(jsonPath("$.data.content.divingShopSection.firstShopInfo.contentId").value(mockDivingShopSection.getFirstShopInfo().getContentId()))
                // 다이빙 업체 소개 섹션 > 2번 다이빙 업체 정보
                .andExpect(jsonPath("$.data.content.divingShopSection.secondShopInfo.title").value(mockDivingShopSection.getSecondShopInfo().getTitle()))
                .andExpect(jsonPath("$.data.content.divingShopSection.secondShopInfo.imageUrl").value(mockDivingShopSection.getSecondShopInfo().getImageUrl()))
                .andExpect(jsonPath("$.data.content.divingShopSection.secondShopInfo.contentId").value(mockDivingShopSection.getSecondShopInfo().getContentId()))
                // 맛집 소개 섹션
                .andExpect(jsonPath("$.data.content.restaurantSection.title").value(mockRestaurantSection.getTitle()))
                .andExpect(jsonPath("$.data.content.restaurantSection.keyword").value(mockRestaurantSection.getKeyword()))
                // 맛집 소개 섹션 > 할인 쿠폰
                .andExpect(jsonPath("$.data.content.restaurantSection.couponSection.title").value(mockRestaurantSection.getCouponSection().getTitle()))
                .andExpect(jsonPath("$.data.content.restaurantSection.couponSection.content").value(mockRestaurantSection.getCouponSection().getContent()))
                .andExpect(jsonPath("$.data.content.restaurantSection.couponSection.eventId").value(mockRestaurantSection.getCouponSection().getEventId()));

        // 맛집 소개 섹션 > 식당/카페 정보
        for(int i = 0 ; i < mockRestaurantSection.getRestaurantList().size(); i++) {
            result.andExpect(jsonPath("$.data.content.restaurantSection.restaurantList[" + i + "].title").value(mockRestaurantSection.getRestaurantList().get(i).getTitle()));
            result.andExpect(jsonPath("$.data.content.restaurantSection.restaurantList[" + i + "].imageUrl").value(mockRestaurantSection.getRestaurantList().get(i).getImageUrl()));
            result.andExpect(jsonPath("$.data.content.restaurantSection.restaurantList[" + i + "].content").value(mockRestaurantSection.getRestaurantList().get(i).getContent()));
            result.andExpect(jsonPath("$.data.content.restaurantSection.restaurantList[" + i + "].contentId").value(mockRestaurantSection.getRestaurantList().get(i).getContentId()));
        }
    }
}