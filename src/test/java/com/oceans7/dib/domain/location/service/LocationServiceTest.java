package com.oceans7.dib.domain.location.service;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.location.dto.response.LocationResponseDto;
import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.global.MockRequest;
import com.oceans7.dib.global.MockResponse;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import com.oceans7.dib.global.api.service.VilageFcstAPIService;
import com.oceans7.dib.global.exception.ApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    @Test
    @DisplayName("좌표로 지역명과 날씨 조회 테스트")
    public void searchPlaceTest() {
        // given
        SearchLocationRequestDto searchLocationReq = MockRequest.testSearchLocationReq();
        int baseX = MockRequest.testBaseX();
        int baseY = MockRequest.testBaseY();
        String nowTimeFormat = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("HH00"));
        String baseDate, baseTime, fcstDate, fcstTime;

        when(kakaoLocalAPIService.getGeoAddressLocalApi(searchLocationReq.getMapX(), searchLocationReq.getMapY()))
                .thenReturn(MockResponse.testGeoAddressRes());

        baseDate = calculateBaseDate(MockRequest.NCST_CALLABLE_TIME);
        baseTime = calculateBaseTime(MockRequest.NCST_CALLABLE_TIME);
        when(vilageFcstAPIService.getNowCast(baseX, baseY, baseDate, baseTime))
                .thenReturn(CompletableFuture.completedFuture(MockResponse.testLocationNcstRes(baseDate, baseTime)));

        fcstDate = calculateBaseDate(MockRequest.FCST_CALLABLE_TIME);
        fcstTime = calculateBaseTime(MockRequest.FCST_CALLABLE_TIME);
        when(vilageFcstAPIService.getUltraForecast(baseX, baseY, fcstDate, fcstTime))
                .thenReturn(CompletableFuture.completedFuture(MockResponse.testLocationFcstRes(baseDate, fcstTime, fcstDate, nowTimeFormat)));

        // when
        LocationResponseDto response = locationService.searchPlace(searchLocationReq);

        // then
        assertThat(response.getAddress()).isEqualTo("서울특별시 중구 창경궁로 17");
        assertThat(response.getWeatherType()).isEqualTo(WeatherType.OVERCAST);
        assertThat(response.getTemperatures()).isEqualTo(26.1);
    }

    private String calculateBaseDate(int callableTime) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if (now.getHour() == 0 && now.getMinute() < callableTime) {
            baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        return baseDate;
    }

    private String calculateBaseTime(int callableTime) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH00");

        return now.getMinute() < callableTime ?
                now.minusHours(1).format(timeFormatter) : now.format(timeFormatter);
    }

    @Test
    @DisplayName("[exception] 잘못된 좌표로 요청 테스트")
    public void searchPlaceInvalidXYThrowsExceptionTest() {
        //given
        SearchLocationRequestDto searchXYExceptionReq = MockRequest.testSearchLocationXYExceptionReq();

        when(kakaoLocalAPIService.getGeoAddressLocalApi(searchXYExceptionReq.getMapX(), searchXYExceptionReq.getMapY()))
                .thenReturn(MockResponse.testGeoAddressXYExceptionRes());

        // when then
        assertThrows(ApplicationException.class, () -> locationService.searchPlace(searchXYExceptionReq));
    }
}
