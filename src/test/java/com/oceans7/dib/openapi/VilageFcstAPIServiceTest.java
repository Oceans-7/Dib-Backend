package com.oceans7.dib.openapi;

import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.response.fcstapi.GetWeatherDigitalForecast;
import com.oceans7.dib.global.api.service.VilageFcstAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class VilageFcstAPIServiceTest {
    @Autowired
    VilageFcstAPIService vilageFcstAPIService;

    private GetWeatherDigitalForecast.LatXLngY grid;
    private int baseX, baseY;

    @BeforeEach
    public void before() {
        double mapX = 126.9779692;
        double mapY = 37.566535;

        this.grid = new GetWeatherDigitalForecast()
                .convertGRID_GPS(mapX, mapY);

        this.baseX = (int)grid.x;
        this.baseY = (int)grid.y;
    }

    @Test
    @DisplayName("초단기 실황 조회 API 통신 테스트")
    public void callUltraSrtNcstAPITest() {
        int callableTime = 40;

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH00");

        String baseTime = now.getMinute() < callableTime ?
                now.minusHours(1).format(timeFormatter) : now.format(timeFormatter);

        // 자정
        if(now.getHour() == 0 && now.getMinute() < callableTime) {
            baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        FcstAPICommonListResponse response = vilageFcstAPIService.getNowCast(baseX, baseY, baseDate, baseTime);

        assertThat(response.getFcstAPICommonItemResponseList().size()).isEqualTo(8);
    }

    @Test
    @DisplayName("초단기 예보 조회 API 통신 테스트")
    public void callUltraFcstAPITest() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH00");

        String baseTime = now.minusHours(1).format(timeFormatter);

        // 자정
        if(now.getHour() == 0) {
            baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        FcstAPICommonListResponse response = vilageFcstAPIService.getUltraForecast(baseX, baseY, baseDate, baseTime);

        assertThat(response.getFcstAPICommonItemResponseList().size()).isEqualTo(60);
    }
}
