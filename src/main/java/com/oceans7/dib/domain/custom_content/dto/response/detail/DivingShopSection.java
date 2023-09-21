package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivingShopSection {
    @JsonProperty("title")
    private String title;

    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("firstShopInfo")
    private ShopInfo firstShopInfo;

    @JsonProperty("secondShopInfo")
    private ShopInfo secondShopInfo;

}
