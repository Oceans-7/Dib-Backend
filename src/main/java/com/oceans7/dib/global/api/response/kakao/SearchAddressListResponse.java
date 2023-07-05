package com.oceans7.dib.global.api.response.kakao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchAddressListResponse {
    private List<SearchAddressItemResponse> searchAddressItemResponseList;

    @JsonCreator
    public SearchAddressListResponse(@JsonProperty("documents") List<SearchAddressItemResponse> searchAddressItemResponseList) {
        this.searchAddressItemResponseList = searchAddressItemResponseList;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchAddressItemResponse {
        private Address address;
        private String address_name;
        private String address_type;
        private Object road_address;
        private double x;
        private double y;

        @JsonCreator
        public SearchAddressItemResponse(@JsonProperty("address") Address address,
                                         @JsonProperty("address_name") String address_name,
                                         @JsonProperty("address_type") String address_type,
                                         @JsonProperty("road_address") Object road_address,
                                         @JsonProperty("x") double x,
                                         @JsonProperty("y") double y) {
            this.address = address;
            this.address_name = address_name;
            this.address_type = address_type;
            this.road_address = road_address;
            this.x = x;
            this.y = y;
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Address {
            private String address_name;
            private String region_1depth_name;
            private String region_2depth_name;

            @JsonCreator
            public Address(@JsonProperty("address_name") String address_name,
                           @JsonProperty("region_1depth_name") String region_1depth_name,
                           @JsonProperty("region_2depth_name") String region_2depth_name) {
                this.address_name = address_name;
                this.region_1depth_name = region_1depth_name;
                this.region_2depth_name = region_2depth_name;
            }
        }
    }
}
