package com.oceans7.dib.domain.openapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.global.MockResponse;
import com.oceans7.dib.global.ResponseWrapper;
import com.oceans7.dib.global.api.http.DataGoKrApi;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonItemResponse;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.service.VilageFcstAPIService;
import com.oceans7.dib.global.util.CoordinateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class VilageFcstAPIServiceTest {
    @Autowired
    VilageFcstAPIService vilageFcstAPIService;

    @MockBean
    private DataGoKrApi dataGoKrApi;

    @Value("${open-api.data-go-kr.service-decode-key}")
    private String serviceKey;

    @Value("${open-api.data-go-kr.data-type}")
    private String dataType;

    private final static int BASE_PAGE = 1;
    private final static int NCST_PAGE_SIZE = 8;
    private final static int FCST_PAGE_SIZE = 60;

    private ResponseWrapper ultraSrtNcstAPIRes;
    private ResponseWrapper ultraFcstAPIRes;

    private ObjectMapper objectMapper;

    private CoordinateUtil.LatXLngY grid;
    private int baseX, baseY;

    @BeforeEach
    public void before() {
        ultraSrtNcstAPIRes = MockResponse.testNcstRes();
        ultraFcstAPIRes = MockResponse.testFcstRes();

        objectMapper = new ObjectMapper();

        double mapX = 126.9779692;
        double mapY = 37.566535;

        this.grid = CoordinateUtil.convertGRID_GPS(mapX, mapY);
        this.baseX = (int)grid.x;
        this.baseY = (int)grid.y;
    }

    @Test
    @DisplayName("초단기 실황 조회 API 통신 테스트")
    public void callUltraSrtNcstAPITest() throws JsonProcessingException {
        // given
        String baseDate = "20230726";
        String baseTime = "0100";

        // when
        String apiResponse = objectMapper.writeValueAsString(ultraSrtNcstAPIRes);
        when(dataGoKrApi.getNowForecastInfo(serviceKey, dataType, baseX, baseY,
                baseDate, baseTime, BASE_PAGE, NCST_PAGE_SIZE))
                .thenReturn(apiResponse);

        FcstAPICommonListResponse list = vilageFcstAPIService.getNowCast(baseX, baseY, baseDate, baseTime);

        // then
        for(FcstAPICommonItemResponse item : list.getFcstAPICommonItemResponseList()) {
            assertThat(item.getBaseDate()).isEqualTo(baseDate);
            assertThat(item.getBaseTime()).isEqualTo(baseTime);
            switch(item.getCategory()) {
                case "PTY" ->  assertThat(item.getObsrValue()).isEqualTo("0");
                case "REH" ->  assertThat(item.getObsrValue()).isEqualTo("89");
                case "RN1" ->  assertThat(item.getObsrValue()).isEqualTo("0");
                case "T1H" ->  assertThat(item.getObsrValue()).isEqualTo("26.1");
                case "UUU" ->  assertThat(item.getObsrValue()).isEqualTo("1");
                case "VEC" ->  assertThat(item.getObsrValue()).isEqualTo("205");
                case "VVV" ->  assertThat(item.getObsrValue()).isEqualTo("2.1");
                case "WSD" ->  assertThat(item.getObsrValue()).isEqualTo("2.3");
            }
        }
    }

    @Test
    @DisplayName("초단기 예보 조회 API 통신 테스트")
    public void callUltraFcstAPITest() throws JsonProcessingException {
        // given
        String baseDate = "20230726";
        String baseTime = "0000";
        String fcstTime = "0100";

        // when
        String apiResponse = objectMapper.writeValueAsString(ultraFcstAPIRes);
        when(dataGoKrApi.getUltraForecastInfo(serviceKey, dataType, baseX, baseY,
                baseDate, baseTime, BASE_PAGE, FCST_PAGE_SIZE))
                .thenReturn(apiResponse);

        FcstAPICommonListResponse list = vilageFcstAPIService.getUltraForecast(baseX, baseY, baseDate, baseTime);

        // then
        for(FcstAPICommonItemResponse item : list.getFcstAPICommonItemResponseList()) {
            assertThat(item.getBaseDate()).isEqualTo(baseDate);
            assertThat(item.getBaseTime()).isEqualTo(baseTime);
            assertThat(item.getFcstDate()).isEqualTo(baseDate);
            assertThat(item.getFcstTime()).isEqualTo(fcstTime);
            switch(item.getCategory()) {
                case "LGT" ->  assertThat(item.getFcstValue()).isEqualTo("0");
                case "PTY" ->  assertThat(item.getFcstValue()).isEqualTo("0");
                case "REH" ->  assertThat(item.getFcstValue()).isEqualTo("85");
                case "RN1" ->  assertThat(item.getFcstValue()).isEqualTo("강수없음");
                case "SKY" ->  assertThat(item.getFcstValue()).isEqualTo("4");
                case "T1H" ->  assertThat(item.getFcstValue()).isEqualTo("25");
                case "UUU" ->  assertThat(item.getFcstValue()).isEqualTo("1.7");
                case "VEC" ->  assertThat(item.getFcstValue()).isEqualTo("228");
                case "VVV" ->  assertThat(item.getFcstValue()).isEqualTo("1.2");
                case "WSD" ->  assertThat(item.getFcstValue()).isEqualTo("2");
            }
        }
    }
}
