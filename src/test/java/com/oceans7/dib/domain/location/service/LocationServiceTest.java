package com.oceans7.dib.domain.location.service;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.location.dto.response.LocationResponseDto;
import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.global.MockRequest;
import com.oceans7.dib.global.MockResponse;
import com.oceans7.dib.global.api.http.KakaoApi;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import com.oceans7.dib.global.api.service.VilageFcstAPIService;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.util.CoordinateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.oceans7.dib.global.MockResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class LocationServiceTest {
    @Autowired
    LocationService locationService;

    @MockBean
    private VilageFcstAPIService vilageFcstAPIService;

    @MockBean
    private KakaoLocalAPIService kakaoLocalAPIService;

    private SearchLocationRequestDto searchLocationReq;
    private SearchLocationRequestDto searchXYExceptionReq;

    private final static int NCST_CALLABLE_TIME = 40;
    private final static int FCST_CALLABLE_TIME = 60;

    private int baseX, baseY;

    @BeforeEach
    public void before() {
        searchLocationReq = MockRequest.testSearchLocationReq();
        searchXYExceptionReq = MockRequest.testSearchLocationXYExceptionReq();

        CoordinateUtil.LatXLngY grid = CoordinateUtil.convertGRID_GPS(MockRequest.X, MockRequest.Y);
        this.baseX = (int)grid.x;
        this.baseY = (int)grid.y;
    }

    @Test
    @DisplayName("좌표로 지역명과 날씨 조회 테스트")
    public void searchPlaceTest() {
        // given
        String baseDate, baseTime;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        when(kakaoLocalAPIService.getGeoAddressLocalApi(searchLocationReq.getMapX(), searchLocationReq.getMapY())).thenReturn(testGeoAddressRes());

        baseDate = calculateBaseDate(now, NCST_CALLABLE_TIME);
        baseTime = calculateBaseTime(now, NCST_CALLABLE_TIME);
        when(vilageFcstAPIService.getNowCast(baseX, baseY, baseDate, baseTime)).thenReturn(testLocationNcstRes());

        baseDate = calculateBaseDate(now, FCST_CALLABLE_TIME);
        baseTime = calculateBaseTime(now, FCST_CALLABLE_TIME);
        when(vilageFcstAPIService.getUltraForecast(baseX, baseY, baseDate, baseTime)).thenReturn(testLocationFcstRes());

        // when
        LocationResponseDto response = locationService.searchPlace(searchLocationReq);

        // then
        assertThat(response.getAddress()).isEqualTo("서울특별시 중구 창경궁로 17");
        assertThat(response.getWeatherType()).isEqualTo(WeatherType.NIGHT_CLOUDY);
        assertThat(response.getTemperatures()).isEqualTo(26.1);
    }

    private String calculateBaseDate(LocalDateTime now, int callableTime) {
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if (now.getHour() == 0 && now.getMinute() < callableTime) {
            baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        return baseDate;
    }

    private String calculateBaseTime(LocalDateTime now, int callableTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH00");
        return now.getMinute() < callableTime ?
                now.minusHours(1).format(timeFormatter) : now.format(timeFormatter);
    }

    @Test
    @DisplayName("[exception] 잘못된 좌표로 요청 테스트")
    public void searchPlaceXYNullExceptionTest() {
        when(kakaoLocalAPIService.getGeoAddressLocalApi(searchXYExceptionReq.getMapX(), searchXYExceptionReq.getMapY()))
                .thenReturn(testGeoAddressXYExceptionRes());

        // then
        assertThrows(ApplicationException.class, () -> locationService.searchPlace(searchXYExceptionReq));
    }
}
