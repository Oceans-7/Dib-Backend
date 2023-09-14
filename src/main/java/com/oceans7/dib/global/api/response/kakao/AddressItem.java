package com.oceans7.dib.global.api.response.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.global.util.CoordinateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressItem {
    @JsonProperty("address")
    private Address address;

    @JsonProperty("road_address")
    private Address roadAddress;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("address_type")
    private String addressType;

    @JsonProperty("x")
    private double x;

    @JsonProperty("y")
    private double y;

    private double distance;

    public void calculateDistance(double requestX, double requestY) {
        this.distance = CoordinateUtil.calculateDistance(requestX, requestY, this.x, this.y);
    }

}
