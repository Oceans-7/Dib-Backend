package com.oceans7.dib.domain.event.service;

import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.Event;
import com.oceans7.dib.domain.event.repository.EventRepository;
import com.oceans7.dib.domain.event.dto.response.CouponSectionResponseDto;
import com.oceans7.dib.domain.event.dto.response.EventResponseDto;
import com.oceans7.dib.domain.event.dto.response.PartnerResponseDto;
import com.oceans7.dib.domain.event.dto.response.PartnerSectionResponseDto;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    private final static int FIRST_SECTION_INDEX = 0;
    private final static int SECOND_SECTION_INDEX = 1;

    @Transactional(readOnly = true)
    public EventResponseDto getEventDetail(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> handleNotFoundException());

        CouponGroup firstCouponGroup = event.getCouponGroups().get(FIRST_SECTION_INDEX);
        CouponGroup secondCouponGroup = event.getCouponGroups().get(SECOND_SECTION_INDEX);

        CouponSectionResponseDto firstCouponSection = CouponSectionResponseDto.from(firstCouponGroup);
        CouponSectionResponseDto secondCouponSection = CouponSectionResponseDto.from(secondCouponGroup);

        PartnerSectionResponseDto partnerSection = makePartnerSectionResponse(firstCouponGroup, secondCouponGroup);

        return EventResponseDto.of(
                event.getEventId(),
                event.getBannerUrl(),
                event.getMainColor(),
                event.getSubColor(),
                firstCouponSection,
                secondCouponSection,
                partnerSection
        );
    }

    private ApplicationException handleNotFoundException() {
        return new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
    }

    private PartnerSectionResponseDto makePartnerSectionResponse(CouponGroup firstCouponGroup, CouponGroup secondCouponGroup) {
        PartnerResponseDto firstPartner = PartnerResponseDto.from(firstCouponGroup);
        PartnerResponseDto secondPartner = PartnerResponseDto.from(secondCouponGroup);

        String partnerSectionKeyword = String.format("%s, %s", firstCouponGroup.getCouponType().getKeyword(), secondCouponGroup.getCouponType().getKeyword());
        String partnerSectionTitle = String.format("%s %s \n할인 참여 업체", firstCouponGroup.getRegion(), partnerSectionKeyword);

        return PartnerSectionResponseDto.of(
                partnerSectionTitle,
                partnerSectionKeyword,
                firstPartner,
                secondPartner
        );
    }
}
