package com.oceans7.dib.domain.event.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponSectionResponseDto {
    @Schema(description = "쿠폰 아이디", example = "0")
    private Long couponGroupId;

    @Schema(description = "쿠폰 이름", example = "제주 서귀포 숙박 할인권")
    private String title;

    @Schema(description = "하이라이트 키워드", example = "숙박")
    private String keyword;

    @Schema(description = "쿠폰 이미지 URL", example = "https://picsum.photos/296/154")
    private String couponImageUrl;

    public static CouponSectionResponseDto of(Long couponGroupId, String title, String keyword, String couponImageUrl) {
        CouponSectionResponseDto couponSectionResponseDto = new CouponSectionResponseDto();

        couponSectionResponseDto.couponGroupId = couponGroupId;
        couponSectionResponseDto.title = title;
        couponSectionResponseDto.keyword = keyword;
        couponSectionResponseDto.couponImageUrl = couponImageUrl;

        return couponSectionResponseDto;
    }
}
