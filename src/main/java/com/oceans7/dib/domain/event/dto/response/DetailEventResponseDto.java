package com.oceans7.dib.domain.event.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailEventResponseDto {
    @Schema(description = "이벤트 ID", example = "0")
    private Long eventId;

    @Schema(description = "대표 이미지 URL", example = "https://picsum.photos/360/530")
    private String firstImageUrl;

    @Schema(description = "메인 색상", example = "FF0770EF")
    private String mainColor;

    @Schema(description = "서브 색상", example = "FFEBF4FE")
    private String subColor;

    private CouponSectionResponseDto firstCouponSection;
    private CouponSectionResponseDto secondCouponSection;

    private PartnerSectionResponseDto partnerSection;

    public static DetailEventResponseDto of(Long eventId, String firstImageUrl, String mainColor, String subColor, CouponSectionResponseDto firstCouponSection, CouponSectionResponseDto secondCouponSection, PartnerSectionResponseDto partnerSection) {
        DetailEventResponseDto detailEventResponseDto = new DetailEventResponseDto();

        detailEventResponseDto.eventId = eventId;
        detailEventResponseDto.firstImageUrl = firstImageUrl;
        detailEventResponseDto.mainColor = mainColor;
        detailEventResponseDto.subColor = subColor;
        detailEventResponseDto.firstCouponSection = firstCouponSection;
        detailEventResponseDto.secondCouponSection = secondCouponSection;
        detailEventResponseDto.partnerSection = partnerSection;

        return detailEventResponseDto;
    }
}
