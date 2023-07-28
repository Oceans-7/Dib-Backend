package com.oceans7.dib.global.api.response.kakao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalResponse {
    @JsonProperty("documents")
    private List<AddressItem> addressItems;

    @JsonCreator
    public LocalResponse(@JsonProperty("documents") List<AddressItem> addressItems) {
        this.addressItems = addressItems;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressItem {
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

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Address {
            @JsonProperty("address_name")
            private String addressName;

            @JsonProperty("region_1depth_name")
            private String region1depthName;

            @JsonProperty("region_2depth_name")
            private String region2depthName;
        }
    }
}
