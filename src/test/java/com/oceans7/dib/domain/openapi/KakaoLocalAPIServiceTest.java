package com.oceans7.dib.domain.openapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.global.api.http.KakaoApi;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


import static com.oceans7.dib.global.MockRequest.*;
import static com.oceans7.dib.global.MockResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class KakaoLocalAPIServiceTest {
    @Autowired
    KakaoLocalAPIService kakaoLocalAPIService;

    @MockBean
    private KakaoApi kakaoApi;

    private LocalResponse searchAddressRes;
    private LocalResponse geoAddressRes;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        searchAddressRes = testSearchAddressRes();
        geoAddressRes = testGeoAddressRes();

        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("카카오 주소 검색 API 통신 테스트")
    public void callSearchAddressAPITest() throws JsonProcessingException {
        // KakaoApi 인터페이스 # getSearchAddress() Mocking
        String apiResponse = objectMapper.writeValueAsString(searchAddressRes);
        when(kakaoApi.getSearchAddress(QUERY)).thenReturn(apiResponse);

        // when
        LocalResponse localResponse = kakaoLocalAPIService.getSearchAddressLocalApi(QUERY);
        LocalResponse.AddressItem addressItem = localResponse.getAddressItems().get(0);
        LocalResponse.AddressItem.Address address = addressItem.getAddress();

        // then
        assertThat(addressItem.getAddressName()).isEqualTo("서울 중구");
        assertThat(addressItem.getX()).isEqualTo(126.997555182293);
        assertThat(addressItem.getY()).isEqualTo(37.5638077703601);
        assertThat(addressItem.getAddressType()).isEqualTo("REGION");

        assertThat(address.getAddressName()).isEqualTo("서울 중구");
        assertThat(address.getRegion1depthName()).isEqualTo("서울");
        assertThat(address.getRegion2depthName()).isEqualTo("중구");
    }

    @Test
    @DisplayName("카카오 좌표 -> 주소 전환 API 통신 테스트")
    public void callGeoAddressAPITest() throws JsonProcessingException {
        // KakaoApi 인터페이스 # getGeoAddress() Mocking
        String apiResponse = objectMapper.writeValueAsString(geoAddressRes);
        when(kakaoApi.getGeoAddress(X, Y)).thenReturn(apiResponse);

        // when
        LocalResponse result = kakaoLocalAPIService.getGeoAddressLocalApi(X, Y);
        LocalResponse.AddressItem.Address address = result.getAddressItems().get(0).getRoadAddress();

        // then
        assertThat(address.getAddressName()).isEqualTo("서울특별시 중구 창경궁로 17");
        assertThat(address.getRegion1depthName()).isEqualTo("서울");
        assertThat(address.getRegion2depthName()).isEqualTo("중구");
    }
}
