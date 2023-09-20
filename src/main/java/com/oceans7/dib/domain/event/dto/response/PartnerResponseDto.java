package com.oceans7.dib.domain.event.dto.response;

import com.oceans7.dib.domain.event.entity.CouponGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerResponseDto {

    @Schema(description = "협력 업체 설명", example = "제주 서귀포")
    private String description;

    @Schema(description = "협력 업체 타입", example = "숙박")
    private String partnerType;

    @Schema(description = "협력 업체 이미지 URL", example = "https://picsum.photos/150/190")
    private String partnerImageUrl;

    @Schema(description = "쿠폰 사용 가능 날짜", example = "2023.11.01")
    private String couponOpenDate;

    public static PartnerResponseDto from(CouponGroup couponGroup) {
        PartnerResponseDto partnerResponseDto = new PartnerResponseDto();

        partnerResponseDto.description = couponGroup.getRegion();
        partnerResponseDto.partnerType = couponGroup.getCouponType().getKeyword();
        partnerResponseDto.partnerImageUrl = couponGroup.getPartnerImageUrl();
        partnerResponseDto.couponOpenDate = couponGroup.getStartDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        return partnerResponseDto;
    }
}
