package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantSection {
    @JsonProperty("title")
    private String title;

    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("restaurantList")
    private List<Restaurant> restaurantList;

    @JsonProperty("couponSection")
    private CouponSection couponSection;

}
