package com.oceans7.dib.domain.home.dto.response.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventResponseDto {
    @Schema(description = "이벤트 ID", example = "0")
    private Long eventId;

    @Schema(description = "배너 이미지 URL", example = "https://picsum.photos/360/530")
    private String bannerImageUrl;

    @Schema(description = "메인 색상", example = "FF0770EF")
    private String mainColor;

    @Schema(description = "서브 색상", example = "FFEBF4FE")
    private String subColor;

    private CouponSectionResponseDto firstCouponSection;
    private CouponSectionResponseDto secondCouponSection;

    private PartnerSectionResponseDto partnerSection;

    public static EventResponseDto of(Long eventId, String bannerImageUrl, String mainColor, String subColor, CouponSectionResponseDto firstCouponSection, CouponSectionResponseDto secondCouponSection, PartnerSectionResponseDto partnerSection) {
        EventResponseDto eventResponseDto = new EventResponseDto();

        eventResponseDto.eventId = eventId;
        eventResponseDto.bannerImageUrl = bannerImageUrl;
        eventResponseDto.mainColor = mainColor;
        eventResponseDto.subColor = subColor;
        eventResponseDto.firstCouponSection = firstCouponSection;
        eventResponseDto.secondCouponSection = secondCouponSection;
        eventResponseDto.partnerSection = partnerSection;

        return eventResponseDto;
    }
}
