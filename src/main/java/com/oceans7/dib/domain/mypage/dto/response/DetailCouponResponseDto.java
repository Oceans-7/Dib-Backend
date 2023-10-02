package com.oceans7.dib.domain.mypage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.CouponStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailCouponResponseDto {

    @Schema(description = "쿠폰 아이디", example = "0")
    private Long couponId;

    @Schema(description = "쿠폰 이미지 URL", example = "https://coupon.image")
    private String couponImageUrl;

    @Schema(description = "쿠폰 해당 지역", example = "제주 서귀포시")
    private String region;

    @Schema(description = "쿠폰 종류", example = "숙박")
    private String couponType;

    @Schema(description = "할인률", example = "10")
    private int discountPercentage;

    @Schema(description = "쿠폰 사용 시작일", example = "2023.09.01")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate startDate;

    @Schema(description = "쿠폰 사용 마감일", example = "2023.10.01")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate closingDate;

    @Schema(description = "쿠폰 남은 기간(만료 쿠폰은 null)", example = "8")
    private Long remainingDays;

    public static DetailCouponResponseDto of(Long couponId, String couponImageUrl, String region, String couponType,
                                             int discountPercentage, LocalDate startDate, LocalDate closingDate, Long remainingDays) {
        DetailCouponResponseDto couponResponseDto = new DetailCouponResponseDto();

        couponResponseDto.couponId = couponId;
        couponResponseDto.couponImageUrl = couponImageUrl;
        couponResponseDto.region = region;
        couponResponseDto.couponType = couponType;
        couponResponseDto.discountPercentage = discountPercentage;

        couponResponseDto.startDate = startDate;
        couponResponseDto.closingDate = closingDate;
        couponResponseDto.remainingDays = remainingDays;

        return couponResponseDto;
    }
}
