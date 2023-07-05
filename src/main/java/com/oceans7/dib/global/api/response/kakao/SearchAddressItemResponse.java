package com.oceans7.dib.global.api.response.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchAddressItemResponse {

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("address_type")
    private String addressType;

    @JsonProperty("x")
    private double x;

    @JsonProperty("y")
    private double y;

}
