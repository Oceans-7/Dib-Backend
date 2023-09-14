package com.oceans7.dib.global.api.response.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("region_1depth_name")
    private String region1depthName;

    @JsonProperty("region_2depth_name")
    private String region2depthName;

}
