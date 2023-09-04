package com.oceans7.dib.domain.openapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.global.MockRequest;
import com.oceans7.dib.global.MockResponse;
import com.oceans7.dib.global.ResponseWrapper;
import com.oceans7.dib.global.api.http.DataGoKrApi;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonItemResponse;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.service.VilageFcstAPIService;
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

    private ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("초단기 실황 조회 API 통신 테스트")
    public void callUltraSrtNcstAPITest() throws JsonProcessingException {
        // given
        String baseDate = "20230726";
        String baseTime = "0100";
        int baseX = MockRequest.testBaseX();
        int baseY = MockRequest.testBaseY();

        // when
        ResponseWrapper ultraSrtNcstAPIRes = MockResponse.testNcstRes();
        String apiResponse = objectMapper.writeValueAsString(ultraSrtNcstAPIRes);
        when(dataGoKrApi.getNowForecastInfo(serviceKey, dataType, baseX, baseY,
                baseDate, baseTime, MockRequest.BASE_PAGE, MockRequest.NCST_PAGE_SIZE))
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
        int baseX = MockRequest.testBaseX();
        int baseY = MockRequest.testBaseY();

        // when
        ResponseWrapper ultraFcstAPIRes = MockResponse.testFcstRes();
        String apiResponse = objectMapper.writeValueAsString(ultraFcstAPIRes);
        when(dataGoKrApi.getUltraForecastInfo(serviceKey, dataType, baseX, baseY,
                baseDate, baseTime, MockRequest.BASE_PAGE, MockRequest.FCST_PAGE_SIZE))
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
