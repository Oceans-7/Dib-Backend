package com.oceans7.dib.domain.mypage.dto.response;

import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.UseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailCouponResponseDto {

    @Schema(description = "쿠폰 아이디", example = "3")
    private Long couponId;

    @Schema(description = "쿠폰 해당 지역", example = "제주 서귀포시")
    private String region;

    @Schema(description = "쿠폰 종류", example = "숙박업소/식당/카페 등")
    private String category;

    @Schema(description = "할인률", example = "10")
    private int discountPercentage;

    @Schema(description = "쿠폰 이름", example = "숙박 업체 10% 할인")
    private String name;

    @Schema(description = "쿠폰 사용 시작일", example = "2023, 09, 01")
    private LocalDate startDate;

    @Schema(description = "쿠폰 사용 마감일", example = "2023, 10, 01")
    private LocalDate closingDate;

    @Schema(description = "쿠폰 남은 기간", example = "8")
    private Long remainingDays;

    @Schema(description = "쿠폰 사용 완료 여부", example = "UNUSED/USED")
    private UseStatus useStatus;

    public static DetailCouponResponseDto of(Coupon coupon) {
        CouponGroup couponGroup = coupon.getCouponGroup();
        DetailCouponResponseDto couponResponseDto = new DetailCouponResponseDto();

        couponResponseDto.couponId = coupon.getCouponId();
        couponResponseDto.useStatus = coupon.getUseStatus();
        couponResponseDto.region = couponGroup.getRegion();
        couponResponseDto.category = couponGroup.getCategory();
        couponResponseDto.discountPercentage = couponGroup.getDiscountPercentage();
        couponResponseDto.name = couponGroup.getName();
        couponResponseDto.startDate = couponGroup.getStartDate();
        couponResponseDto.closingDate = couponGroup.getClosingDate();
        couponResponseDto.remainingDays = Duration.between(couponGroup.getStartDate().atStartOfDay(), couponGroup.getClosingDate().atStartOfDay()).toDays();

        return couponResponseDto;
    }
}
