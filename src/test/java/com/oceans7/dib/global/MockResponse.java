package com.oceans7.dib.global;

import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.response.kakao.LocalResponse.*;
import com.oceans7.dib.global.api.response.kakao.LocalResponse.AddressItem.*;

import java.util.ArrayList;
import java.util.List;

public class MockResponse {
    private static final String ROAD_ADDRESS_NAME = "서울특별시 중구 창경궁로 17";
    private static final String ADDRESS_NAME = "서울 중구";
    private static final String REGION_1DEPTH_NAME = "서울";
    private static final String REGION_2DEPTH_NAME = "중구";
    private static final double X = 126.997555182293;
    private static final double Y = 37.5638077703601;
    private static final String ADDRESS_TYPE = "REGION";

    private static Address setAddress() {
        return new Address(ADDRESS_NAME, REGION_1DEPTH_NAME, REGION_2DEPTH_NAME);
    }

    private static Address setRoadAddress() {
        return new Address(ROAD_ADDRESS_NAME, REGION_1DEPTH_NAME, REGION_2DEPTH_NAME);
    }

    private static AddressItem setAddressItem(Address address, Address roadAddress) {
        return new AddressItem(address, roadAddress, ADDRESS_NAME, ADDRESS_TYPE, X, Y);
    }

    public static LocalResponse testSearchAddressRes() {
        List<AddressItem> item = new ArrayList<>();
        item.add(setAddressItem(setAddress(), null));
        return new LocalResponse(item);
    }

    public static LocalResponse testGeoAddressRes() {
        List<AddressItem> item = new ArrayList<>();
        item.add(setAddressItem(null, setRoadAddress()));
        return new LocalResponse(item);
    }
}
