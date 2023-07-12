package com.oceans7.dib.global.api.response.kakao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalResponse {
    private List<AddressItem> addressItems;

    @JsonCreator
    public LocalResponse(@JsonProperty("documents") List<AddressItem> addressItems) {
        this.addressItems = addressItems;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressItem {
        private Address address;
        private RoadAddress roadAddress;
        private String addressName;
        private String addressType;
        private double x;
        private double y;

        @JsonCreator
        public AddressItem(@JsonProperty("address") Address address,
                           @JsonProperty("road_address") RoadAddress roadAddress,
                           @JsonProperty("address_name") String addressName,
                           @JsonProperty("address_type") String addressType,
                           @JsonProperty("x") double x,
                           @JsonProperty("y") double y) {
            this.address = address;
            this.roadAddress = roadAddress;
            this.addressName = addressName;
            this.addressType = addressType;
            this.x = x;
            this.y = y;
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Address {
            /**
             * 지역 검색 시 Address에 담겨옴.
             */
            private String addressName;
            private String region1depthName;
            private String region2depthName;

            @JsonCreator
            public Address(@JsonProperty("address_name") String addressName,
                           @JsonProperty("region_1depth_name") String region1depthName,
                           @JsonProperty("region_2depth_name") String region2depthName) {
                this.addressName = addressName;
                this.region1depthName = region1depthName;
                this.region2depthName = region2depthName;
            }
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class RoadAddress {
            /**
             * 도로명 주소로 검색 시 RoadAddress에 담겨옴.
             */
            private String addressName;
            private String region1depthName;
            private String region2depthName;

            @JsonCreator
            public RoadAddress(@JsonProperty("address_name") String addressName,
                               @JsonProperty("region_1depth_name") String region1depthName,
                               @JsonProperty("region_2depth_name") String region2depthName) {
                this.addressName = addressName;
                this.region1depthName = region1depthName;
                this.region2depthName = region2depthName;
            }
        }
    }
}