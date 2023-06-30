package com.oceans7.dib.openapi;

import com.oceans7.dib.domain.weather.FcstType;
import com.oceans7.dib.openapi.dto.response.fcstapi.FcstAPICommonItemResponse;
import com.oceans7.dib.openapi.dto.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.openapi.service.FcstInfoAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class FcstInfoAPIServiceTest {
    @Autowired
    FcstInfoAPIService fcstInfoAPIService;

    @Test
    @DisplayName("초단기 실황 조회 API 통신 테스트")
    public void callUltraSrtNcstAPITest() {
        double mapX = 126.98935225645432;
        double mapY = 37.579871128849334;

        FcstAPICommonListResponse response = fcstInfoAPIService.fetchDataFromNowForecast(mapX, mapY);

        for(FcstAPICommonItemResponse item : response.getFcstAPICommonItemResponseList()) {
            FcstType type = FcstType.valueOf(item.getCategory());
            System.out.println(type.getDescription() + " : " + item.getObsrValue() + type.getUnit());
        }

        assertThat(response.getFcstAPICommonItemResponseList().size()).isEqualTo(8);

    }
}
