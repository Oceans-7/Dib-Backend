package com.oceans7.dib.domain.event.service;

import com.oceans7.dib.domain.event.dto.response.*;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.Event;
import com.oceans7.dib.domain.event.repository.CouponGroupRepository;
import com.oceans7.dib.domain.event.repository.EventRepository;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EventServiceTest {
    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CouponGroupRepository couponGroupRepository;

    private Event makeEvent() {
        return eventRepository.save(MockEntity.testEvent());
    }

    private CouponGroup makeFirstCouponGroup(Event event) {
        CouponGroup couponGroup = MockEntity.testCouponGroup(event);
        return couponGroupRepository.save(couponGroup);
    }

    private CouponGroup makeSecondCouponGroup(Event event) {
        CouponGroup couponGroup = MockEntity.testCouponGroup2(event);
        return couponGroupRepository.save(couponGroup);
    }

    @Test
    @DisplayName("이벤트 리스트 조회")
    public void getAllEvent() {
        // given
        Event event = makeEvent();

        // when
        List<EventResponseDto> eventList = eventService.getAllEvent();

        // then
        // 이벤트는 단일 레코드로 존재한다고 가정
        EventResponseDto mockResponse = MockResponse.testEventRes(event).get(0);
        EventResponseDto response = eventList.get(0);
        assertThat(response.getEventId()).isEqualTo(mockResponse.getEventId());
        assertThat(response.getBannerImageUrl()).isEqualTo(mockResponse.getBannerImageUrl());
    }

    @Test
    @DisplayName("이벤트 리스트 조회 : 진행중인 이벤트 없는 경우")
    public void getAllEventIfNoEventsInProgress() {
        // when
        List<EventResponseDto> response = eventService.getAllEvent();

        // then
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("이벤트 상세 조회")
    public void getEventDetail() {
        // given
        Event event = makeEvent();
        CouponGroup firstCouponGroup = makeFirstCouponGroup(event);
        CouponGroup secondCouponGroup = makeSecondCouponGroup(event);

        // when
        DetailEventResponseDto response = eventService.getEventDetail(event.getEventId());

        // then
        // 이벤트 검증
        DetailEventResponseDto detailEventMockResponse = MockResponse.testDetailEventRes(event, firstCouponGroup, secondCouponGroup);
        assertThat(response.getEventId()).isEqualTo(detailEventMockResponse.getEventId());
        assertThat(response.getMainColor()).isEqualTo(detailEventMockResponse.getMainColor());
        assertThat(response.getSubColor()).isEqualTo(detailEventMockResponse.getSubColor());
        assertThat(response.getFirstImageUrl()).isEqualTo(detailEventMockResponse.getFirstImageUrl());

        // 쿠폰 검증
        CouponSectionResponseDto firstCouponSection = response.getFirstCouponSection();
        assertThat(firstCouponSection.getCouponGroupId()).isEqualTo(detailEventMockResponse.getFirstCouponSection().getCouponGroupId());
        assertThat(firstCouponSection.getTitle()).isEqualTo(detailEventMockResponse.getFirstCouponSection().getTitle());
        assertThat(firstCouponSection.getKeyword()).isEqualTo(detailEventMockResponse.getFirstCouponSection().getKeyword());
        assertThat(firstCouponSection.getCouponImageUrl()).isEqualTo(detailEventMockResponse.getFirstCouponSection().getCouponImageUrl());

        CouponSectionResponseDto secondCouponSection = response.getSecondCouponSection();
        assertThat(secondCouponSection.getCouponGroupId()).isEqualTo(detailEventMockResponse.getSecondCouponSection().getCouponGroupId());
        assertThat(secondCouponSection.getTitle()).isEqualTo(detailEventMockResponse.getSecondCouponSection().getTitle());
        assertThat(secondCouponSection.getKeyword()).isEqualTo(detailEventMockResponse.getSecondCouponSection().getKeyword());
        assertThat(secondCouponSection.getCouponImageUrl()).isEqualTo(detailEventMockResponse.getSecondCouponSection().getCouponImageUrl());

        // 협력 업체 검증
        PartnerSectionResponseDto partnerSection = response.getPartnerSection();
        assertThat(partnerSection.getKeyword()).isEqualTo(detailEventMockResponse.getPartnerSection().getKeyword());
        assertThat(partnerSection.getTitle()).isEqualTo(detailEventMockResponse.getPartnerSection().getTitle());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        PartnerResponseDto firstPartnerResponse = partnerSection.getFirstPartner();
        assertThat(firstPartnerResponse.getDescription()).isEqualTo(detailEventMockResponse.getPartnerSection().getFirstPartner().getDescription());
        assertThat(firstPartnerResponse.getPartnerType()).isEqualTo(detailEventMockResponse.getPartnerSection().getFirstPartner().getPartnerType());
        assertThat(firstPartnerResponse.getPartnerImageUrl()).isEqualTo(detailEventMockResponse.getPartnerSection().getFirstPartner().getPartnerImageUrl());
        assertThat(firstPartnerResponse.getCouponOpenDate()).isEqualTo(detailEventMockResponse.getPartnerSection().getFirstPartner().getCouponOpenDate());

        PartnerResponseDto secondPartnerResponse = partnerSection.getSecondPartner();
        assertThat(secondPartnerResponse.getDescription()).isEqualTo(detailEventMockResponse.getPartnerSection().getSecondPartner().getDescription());
        assertThat(secondPartnerResponse.getPartnerType()).isEqualTo(detailEventMockResponse.getPartnerSection().getSecondPartner().getPartnerType());
        assertThat(secondPartnerResponse.getPartnerImageUrl()).isEqualTo(detailEventMockResponse.getPartnerSection().getSecondPartner().getPartnerImageUrl());
        assertThat(secondPartnerResponse.getCouponOpenDate()).isEqualTo(detailEventMockResponse.getPartnerSection().getSecondPartner().getCouponOpenDate());
    }
}
