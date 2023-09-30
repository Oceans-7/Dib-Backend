package com.oceans7.dib.domain.mypage.dto.response;

import com.oceans7.dib.global.util.ValidatorUtil;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponResponseDto {
    @Schema(description = "보유 쿠폰 개수", example = "5")
    private int count;

    @ArraySchema(schema = @Schema(description = "쿠폰 정보", implementation = DetailCouponResponseDto.class))
    private List<DetailCouponResponseDto> couponList;

    public static CouponResponseDto from(List<DetailCouponResponseDto> couponList) {
        CouponResponseDto couponResponseDto = new CouponResponseDto();

        couponResponseDto.count = ValidatorUtil.isEmpty(couponList) ? 0 : couponList.size();
        couponResponseDto.couponList = ValidatorUtil.isEmpty(couponList) ? null : couponList;

        return couponResponseDto;
    }
}
