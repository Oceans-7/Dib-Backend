package com.oceans7.dib.domain.mypage.dto.response;

import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.CouponStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailCouponResponseDto {

    @Schema(description = "쿠폰 아이디", example = "0")
    private Long couponId;

    @Schema(description = "쿠폰 해당 지역", example = "제주 서귀포시")
    private String region;

    @Schema(description = "쿠폰 종류", example = "숙박")
    private String couponType;

    @Schema(description = "할인률", example = "10")
    private int discountPercentage;

    @Schema(description = "쿠폰 사용 시작일", example = "2023.09.01")
    private String startDate;

    @Schema(description = "쿠폰 사용 마감일", example = "2023.10.01")
    private String closingDate;

    @Schema(description = "쿠폰 남은 기간(만료 쿠폰은 null)", example = "8")
    private Long remainingDays;

    @Schema(description = "쿠폰 사용 완료 여부", example = "false")
    private boolean isUsed;

    public static DetailCouponResponseDto from(Coupon coupon) {
        CouponGroup couponGroup = coupon.getCouponGroup();
        DetailCouponResponseDto couponResponseDto = new DetailCouponResponseDto();

        couponResponseDto.couponId = coupon.getCouponId();
        couponResponseDto.isUsed = coupon.getStatus() == CouponStatus.USED ? true : false;
        couponResponseDto.region = couponGroup.getRegion();
        couponResponseDto.couponType = couponGroup.getCouponType().getKeyword();
        couponResponseDto.discountPercentage = couponGroup.getDiscountPercentage();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        couponResponseDto.startDate = couponGroup.getStartDate().format(formatter);
        couponResponseDto.closingDate = couponGroup.getClosingDate().format(formatter);
        couponResponseDto.remainingDays = Duration.between(couponGroup.getStartDate().atStartOfDay(), couponGroup.getClosingDate().atStartOfDay()).toDays();

        return couponResponseDto;
    }
}
