package com.oceans7.dib.domain.home.service;

import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.Event;
import com.oceans7.dib.domain.event.repository.CouponGroupRepository;
import com.oceans7.dib.domain.event.repository.EventRepository;
import com.oceans7.dib.domain.home.dto.response.event.CouponSectionResponseDto;
import com.oceans7.dib.domain.home.dto.response.event.EventResponseDto;
import com.oceans7.dib.domain.home.dto.response.event.PartnerResponseDto;
import com.oceans7.dib.domain.home.dto.response.event.PartnerSectionResponseDto;
import com.oceans7.dib.global.MockRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class HomeServiceTest {
    @Autowired
    private HomeService homeService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CouponGroupRepository couponGroupRepository;

    private Event makeEvent() {
        return eventRepository.save(MockRequest.testEvent());
    }

    private CouponGroup makeFirstCouponGroup(Event event) {
        CouponGroup couponGroup = MockRequest.testCouponGroup();
        couponGroup.setEvent(event);
        return couponGroupRepository.save(couponGroup);
    }

    private CouponGroup makeSecondCouponGroup(Event event) {
        CouponGroup couponGroup = MockRequest.testCouponGroup2();
        couponGroup.setEvent(event);
        return couponGroupRepository.save(couponGroup);
    }

    @Test
    @DisplayName("이벤트 조회")
    public void getEventDetail() {
        // given
        Event event = makeEvent();
        CouponGroup firstCouponGroup = makeFirstCouponGroup(event);
        CouponGroup secondCouponGroup = makeSecondCouponGroup(event);

        // when
        EventResponseDto response = homeService.getEventDetail(event.getEventId());

        // then
        // 이벤트 검증
        assertThat(response.getEventId()).isEqualTo(event.getEventId());
        assertThat(response.getMainColor()).isEqualTo(event.getMainColor());
        assertThat(response.getSubColor()).isEqualTo(event.getSubColor());
        assertThat(response.getBannerImageUrl()).isEqualTo(event.getBannerUrl());

        // 쿠폰 검증
        CouponSectionResponseDto firstCouponSection = response.getFirstCouponSection();
        assertThat(firstCouponSection.getCouponGroupId()).isEqualTo(firstCouponGroup.getCouponGroupId());
        assertThat(firstCouponSection.getTitle()).isEqualTo(firstCouponGroup.getName());
        assertThat(firstCouponSection.getKeyword()).isEqualTo(firstCouponGroup.getCouponType().getKeyword());
        assertThat(firstCouponSection.getCouponImageUrl()).isEqualTo(firstCouponGroup.getCouponImageUrl());

        CouponSectionResponseDto secondCouponSection = response.getSecondCouponSection();
        assertThat(secondCouponSection.getCouponGroupId()).isEqualTo(secondCouponGroup.getCouponGroupId());
        assertThat(secondCouponSection.getTitle()).isEqualTo(secondCouponGroup.getName());
        assertThat(secondCouponSection.getKeyword()).isEqualTo(secondCouponGroup.getCouponType().getKeyword());
        assertThat(secondCouponSection.getCouponImageUrl()).isEqualTo(secondCouponGroup.getCouponImageUrl());

        // 협력 업체 검증
        PartnerSectionResponseDto partnerSection = response.getPartnerSection();
        String partnerSectionKeyword = String.format("%s, %s", firstCouponGroup.getCouponType().getKeyword(), secondCouponGroup.getCouponType().getKeyword());
        String partnerSectionTitle = String.format("%s %s \n할인 참여 업체", firstCouponGroup.getRegion(), partnerSectionKeyword);

        assertThat(partnerSection.getKeyword()).isEqualTo(partnerSectionKeyword);
        assertThat(partnerSection.getTitle()).isEqualTo(partnerSectionTitle);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        PartnerResponseDto firstPartnerResponse = partnerSection.getFirstPartner();
        assertThat(firstPartnerResponse.getDescription()).isEqualTo(firstCouponGroup.getRegion());
        assertThat(firstPartnerResponse.getPartnerType()).isEqualTo(firstCouponGroup.getCouponType().getKeyword());
        assertThat(firstPartnerResponse.getPartnerImageUrl()).isEqualTo(firstCouponGroup.getPartnerImageUrl());
        assertThat(firstPartnerResponse.getCouponOpenDate()).isEqualTo(firstCouponGroup.getStartDate().format(formatter));

        PartnerResponseDto secondPartnerResponse = partnerSection.getSecondPartner();
        assertThat(secondPartnerResponse.getDescription()).isEqualTo(secondCouponGroup.getRegion());
        assertThat(secondPartnerResponse.getPartnerType()).isEqualTo(secondCouponGroup.getCouponType().getKeyword());
        assertThat(secondPartnerResponse.getPartnerImageUrl()).isEqualTo(secondCouponGroup.getPartnerImageUrl());
        assertThat(secondPartnerResponse.getCouponOpenDate()).isEqualTo(secondCouponGroup.getStartDate().format(formatter));
    }
}
