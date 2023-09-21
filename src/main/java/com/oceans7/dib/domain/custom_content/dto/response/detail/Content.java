package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Content {
    @JsonProperty("titleSection")
    private TitleSection titleSection;

    @JsonProperty("regionSection")
    private RegionSection regionSection;

    @JsonProperty("divingPointSection")
    private DivingPointSection divingPointSection;

    @JsonProperty("divingShopSection")
    private DivingShopSection divingShopSection;

    @JsonProperty("restaurantSection")
    private RestaurantSection restaurantSection;
}
