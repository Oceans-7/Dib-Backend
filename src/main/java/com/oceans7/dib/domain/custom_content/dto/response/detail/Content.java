package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Content {
    @JsonProperty("titleSection")
    @Schema(description = "제목 섹션", implementation = TitleSection.class)
    private TitleSection titleSection;

    @JsonProperty("regionSection")
    @Schema(description = "지역 소개 섹션", implementation = RegionSection.class)
    private RegionSection regionSection;

    @JsonProperty("divingPointSection")
    @Schema(description = "다이빙 포인트 소개 섹션", implementation = DivingPointSection.class)
    private DivingPointSection divingPointSection;

    @JsonProperty("divingShopSection")
    @Schema(description = "다이빙 업체 소개 섹션", implementation = DivingShopSection.class)
    private DivingShopSection divingShopSection;

    @JsonProperty("restaurantSection")
    @Schema(description = "맛집 소개 섹션", implementation = RestaurantSection.class)
    private RestaurantSection restaurantSection;

    @JsonProperty("couponSection")
    @Schema(description = "할인 쿠폰 섹션", implementation = CouponSection.class)
    private CouponSection couponSection;
}
