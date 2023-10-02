package com.oceans7.dib.domain.event.service;

import com.oceans7.dib.domain.event.dto.response.*;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.Event;
import com.oceans7.dib.domain.event.repository.EventRepository;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.ImageAssetUrlProcessor;
import com.oceans7.dib.global.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;

    private final ImageAssetUrlProcessor imageAssetUrlProcessor;

    private final static int FIRST_SECTION_INDEX = 0;
    private final static int SECOND_SECTION_INDEX = 1;

    /**
     * 이벤트 조회
     */
    public List<EventResponseDto> getAllEvent() {
        List<Event> eventList = eventRepository.findAll();

        if(ValidatorUtil.isEmpty(eventList)) {
            return new ArrayList<>();
        }

        return eventList.stream().map(event -> EventResponseDto.of(
                event.getEventId(),
                imageAssetUrlProcessor.prependCloudFrontHost(event.getBannerImageUrl())
        )).collect(Collectors.toList());
    }

    /**
     * 이벤트 상세 콘텐츠 조회
     */
    public DetailEventResponseDto getEventDetail(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> handleNotFoundException());

        CouponGroup firstCouponGroup = event.getCouponGroupList().get(FIRST_SECTION_INDEX);
        CouponGroup secondCouponGroup = event.getCouponGroupList().get(SECOND_SECTION_INDEX);

        CouponSectionResponseDto firstCouponSection = makeCouponSectionResponse(firstCouponGroup);
        CouponSectionResponseDto secondCouponSection = makeCouponSectionResponse(secondCouponGroup);

        PartnerSectionResponseDto partnerSection = makePartnerSectionResponse(firstCouponGroup, secondCouponGroup);

        return DetailEventResponseDto.of(
                event.getEventId(),
                imageAssetUrlProcessor.prependCloudFrontHost(event.getFirstImageUrl()),
                event.getMainColor(),
                event.getSubColor(),
                firstCouponSection,
                secondCouponSection,
                partnerSection
        );
    }

    private CouponSectionResponseDto makeCouponSectionResponse(CouponGroup couponGroup) {
        return CouponSectionResponseDto.of(
                couponGroup.getCouponGroupId(),
                couponGroup.getName(),
                couponGroup.getCouponType().getKeyword(),
                imageAssetUrlProcessor.prependCloudFrontHost(couponGroup.getCouponImageUrl())
        );
    }
    private ApplicationException handleNotFoundException() {
        return new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
    }

    private PartnerSectionResponseDto makePartnerSectionResponse(CouponGroup firstCouponGroup, CouponGroup secondCouponGroup) {
        PartnerResponseDto firstPartner = makePartnerResponse(firstCouponGroup);
        PartnerResponseDto secondPartner = makePartnerResponse(secondCouponGroup);

        String partnerSectionKeyword = String.format("%s, %s", firstCouponGroup.getCouponType().getKeyword(), secondCouponGroup.getCouponType().getKeyword());
        String partnerSectionTitle = String.format("%s %s 할인 참여 업체", firstCouponGroup.getRegion(), partnerSectionKeyword);

        return PartnerSectionResponseDto.of(
                partnerSectionTitle,
                partnerSectionKeyword,
                firstPartner,
                secondPartner
        );
    }

    private PartnerResponseDto makePartnerResponse(CouponGroup couponGroup) {
        return PartnerResponseDto.of(
                couponGroup.getRegion(),
                couponGroup.getCouponType().getKeyword(),
                imageAssetUrlProcessor.prependCloudFrontHost(couponGroup.getPartnerImageUrl()),
                couponGroup.getStartDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        );
    }
}
