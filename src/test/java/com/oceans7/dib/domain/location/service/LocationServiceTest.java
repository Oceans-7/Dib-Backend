package com.oceans7.dib.domain.location.service;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.location.dto.response.LocationResponseDto;
import com.oceans7.dib.global.MockRequest;
import com.oceans7.dib.global.exception.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class LocationServiceTest {
    @Autowired
    LocationService locationService;

    private SearchLocationRequestDto searchLocationRequestDto;
    private SearchLocationRequestDto searchXYExceptionRequestDto;

    @BeforeEach
    public void before() {
        searchLocationRequestDto = MockRequest.testSearchLocationReq();
        searchXYExceptionRequestDto = MockRequest.testSearchLocationXYExceptionReq();
    }

    @Test
    @DisplayName("좌표로 지역명과 날씨 조회 테스트")
    public void searchPlaceTest() {

        LocationResponseDto response = locationService.searchPlace(searchLocationRequestDto);

        assertThat(response.getAddress()).isEqualTo("서울특별시 중구 세종대로 110");
        assertThat(response.getWeatherType()).isNotNull();
        assertThat(response.getTemperatures()).isBetween(-10.0, 35.0);
    }

    @Test
    @DisplayName("exception 잘못된 좌표로 조회하는 경우")
    public void searchPlaceXYNullExceptionTest() {
        assertThrows(ApplicationException.class, () -> locationService.searchPlace(searchXYExceptionRequestDto));
    }
}
