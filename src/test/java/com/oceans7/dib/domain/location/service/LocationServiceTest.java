package com.oceans7.dib.domain.location.service;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.location.dto.response.LocationResponseDto;
import com.oceans7.dib.global.MockRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient(timeout = "1500")
public class LocationServiceTest {
    @Autowired
    LocationService locationService;

    private SearchLocationRequestDto searchLocationRequestDto;

    @BeforeEach
    public void before() {
        searchLocationRequestDto = MockRequest.testSearchLocationReq();
    }

    @Test
    @DisplayName("좌표 기준 지역명, 날씨 검색 테스트")
    public void searchPlaceTest() {

        LocationResponseDto response = locationService.searchPlace(searchLocationRequestDto);

        assertThat(response.getAddress()).isEqualTo("서울특별시 중구 세종대로 110");
        assertThat(response.getWeatherType()).isNotNull();
        assertThat(response.getTemperatures()).isBetween(-10.0, 35.0);
    }
}
