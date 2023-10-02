package com.oceans7.dib.domain.custom_content.service;

import com.oceans7.dib.domain.custom_content.dto.response.ContentResponseDto;
import com.oceans7.dib.domain.custom_content.dto.response.detail.*;
import com.oceans7.dib.domain.custom_content.entity.CustomContent;
import com.oceans7.dib.domain.custom_content.repository.CustomContentRepository;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CustomContentServiceTest {
    @Autowired
    private CustomContentService customContentService;

    @Autowired
    private CustomContentRepository customContentRepository;

    private CustomContent makeCustomContent() {
        return customContentRepository.save(MockEntity.testCustomContent());
    }

    @Test
    @DisplayName("자체 콘텐츠 리스트 조회")
    public void getAllCustomContent() {
        // given
        CustomContent customContent = makeCustomContent();

        // when
        List<ContentResponseDto> response = customContentService.getAllCustomContent();

        // then
        ContentResponseDto mockResponse = MockResponse.testContentRes(customContent);
        assertThat(response.get(0).getCustomContentId()).isEqualTo(mockResponse.getCustomContentId());
        assertThat(response.get(0).getTitle()).isEqualTo(mockResponse.getTitle());
        assertThat(response.get(0).getSubTitle()).isEqualTo(mockResponse.getSubTitle());
        assertThat(response.get(0).getFirstImageUrl()).isEqualTo(mockResponse.getFirstImageUrl());
    }

    @Test
    @DisplayName("자체 콘텐츠 리스트 조회 : 등록된 자체 콘텐츠가 없는 경우")
    public void getAllCustomContentIfNotRegisteredContent() {
        // when
        List<ContentResponseDto> response = customContentService.getAllCustomContent();

        // then
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("자체 콘텐츠 상세 조회")
    public void getDetailCustomContent() {
        // given
        CustomContent customContent = makeCustomContent();

        // when
        DetailContentResponseDto response = customContentService.getDetailCustomContent(customContent.getCustomContentId());

        // then
        assertThat(response.getCustomContentId()).isEqualTo(customContent.getCustomContentId());
        assertThat(response.getContentTitle()).isEqualTo(String.format("%s %s", customContent.getTitle(), "콘텐츠"));

        // 제목 섹션
        TitleSection titleSection = response.getContent().getTitleSection();
        assertThat(titleSection.getTitle()).isEqualTo(MockResponse.testContentRes().getTitleSection().getTitle());
        assertThat(titleSection.getSubTitle()).isEqualTo(MockResponse.testContentRes().getTitleSection().getSubTitle());
        assertThat(titleSection.getThumbnailUrl()).isEqualTo(MockResponse.testContentRes().getTitleSection().getThumbnailUrl());
        assertThat(titleSection.getRegion()).isEqualTo(MockResponse.testContentRes().getTitleSection().getRegion());

        // 지역 소개 섹션
        RegionSection regionSection = response.getContent().getRegionSection();
        assertThat(regionSection.getTitle()).isEqualTo(MockResponse.testContentRes().getRegionSection().getTitle());
        assertThat(regionSection.getImageTopContent()).isEqualTo(MockResponse.testContentRes().getRegionSection().getImageTopContent());
        assertThat(regionSection.getImageUrl()).isEqualTo(MockResponse.testContentRes().getRegionSection().getImageUrl());
        assertThat(regionSection.getImageBottomContent()).isEqualTo(MockResponse.testContentRes().getRegionSection().getImageBottomContent());
        assertThat(regionSection.getKeyword()).isEqualTo(MockResponse.testContentRes().getRegionSection().getKeyword());

        // 다이빙 포인트 소개 섹션
        DivingPointSection divingPointSection = response.getContent().getDivingPointSection();
        assertThat(divingPointSection.getTitle()).isEqualTo(MockResponse.testContentRes().getDivingPointSection().getTitle());
        assertThat(divingPointSection.getImageUrl()).isEqualTo(MockResponse.testContentRes().getDivingPointSection().getImageUrl());
        assertThat(divingPointSection.getSubTitle()).isEqualTo(MockResponse.testContentRes().getDivingPointSection().getSubTitle());
        assertThat(divingPointSection.getContent()).isEqualTo(MockResponse.testContentRes().getDivingPointSection().getContent());
        assertThat(divingPointSection.getKeyword()).isEqualTo(MockResponse.testContentRes().getDivingPointSection().getKeyword());

        // 다이빙 업체 소개 섹션
        DivingShopSection divingShopSection = response.getContent().getDivingShopSection();
        assertThat(divingShopSection.getTitle()).isEqualTo(MockResponse.testContentRes().getDivingShopSection().getTitle());
        assertThat(divingShopSection.getKeyword()).isEqualTo(MockResponse.testContentRes().getDivingShopSection().getKeyword());
        assertThat(divingShopSection.getFirstShopInfo().getTitle()).isEqualTo(MockResponse.testContentRes().getDivingShopSection().getFirstShopInfo().getTitle());
        assertThat(divingShopSection.getFirstShopInfo().getImageUrl()).isEqualTo(MockResponse.testContentRes().getDivingShopSection().getFirstShopInfo().getImageUrl());
        assertThat(divingShopSection.getFirstShopInfo().getContentId()).isEqualTo(MockResponse.testContentRes().getDivingShopSection().getFirstShopInfo().getContentId());
        assertThat(divingShopSection.getSecondShopInfo().getTitle()).isEqualTo(MockResponse.testContentRes().getDivingShopSection().getSecondShopInfo().getTitle());
        assertThat(divingShopSection.getSecondShopInfo().getImageUrl()).isEqualTo(MockResponse.testContentRes().getDivingShopSection().getSecondShopInfo().getImageUrl());
        assertThat(divingShopSection.getSecondShopInfo().getContentId()).isEqualTo(MockResponse.testContentRes().getDivingShopSection().getSecondShopInfo().getContentId());

        // 맛집 소개 섹션
        RestaurantSection restaurantSection = response.getContent().getRestaurantSection();
        assertThat(restaurantSection.getTitle()).isEqualTo(MockResponse.testContentRes().getRestaurantSection().getTitle());
        assertThat(restaurantSection.getKeyword()).isEqualTo(MockResponse.testContentRes().getRestaurantSection().getKeyword());
        List<Restaurant> restaurantList = restaurantSection.getRestaurantList();
        for(int i = 0; i < restaurantList.size(); i++) {
            assertThat(restaurantList.get(i).getTitle()).isEqualTo(MockResponse.testContentRes().getRestaurantSection().getRestaurantList().get(i).getTitle());
            assertThat(restaurantList.get(i).getImageUrl()).isEqualTo(MockResponse.testContentRes().getRestaurantSection().getRestaurantList().get(i).getImageUrl());
            assertThat(restaurantList.get(i).getContent()).isEqualTo(MockResponse.testContentRes().getRestaurantSection().getRestaurantList().get(i).getContent());
            assertThat(restaurantList.get(i).getContentId()).isEqualTo(MockResponse.testContentRes().getRestaurantSection().getRestaurantList().get(i).getContentId());
        }

        CouponSection couponSection = response.getContent().getCouponSection();
        assertThat(couponSection.getTitle()).isEqualTo(MockResponse.testContentRes().getCouponSection().getTitle());
        assertThat(couponSection.getCouponImageUrl()).isEqualTo(MockResponse.testContentRes().getCouponSection().getCouponImageUrl());
        assertThat(couponSection.getEventId()).isEqualTo(MockResponse.testContentRes().getCouponSection().getEventId());
    }

}
