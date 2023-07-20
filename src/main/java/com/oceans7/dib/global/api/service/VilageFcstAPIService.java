package com.oceans7.dib.global.api.service;

import com.oceans7.dib.global.api.http.DataGoKrApi;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class VilageFcstAPIService extends OpenAPIService {

    private final DataGoKrApi dataGoKrApi;

    public VilageFcstAPIService(DataGoKrApi dataGoKrApi) {
        this.dataGoKrApi = dataGoKrApi;
    }

    @Value("${open-api.data-go-kr.service-decode-key}")
    private String serviceKey;

    @Value("${open-api.data-go-kr.data-type}")
    private String dataType;

    private final static int BASE_PAGE = 1;
    private final static int NCST_PAGE_SIZE = 8;
    private final static int FCST_PAGE_SIZE = 60;

    /**
     * 기상청 초단기 실황
     */
    public FcstAPICommonListResponse getNowCast(int x, int y, String baseDate, String baseTime) {

        String result = dataGoKrApi.getNowForecastInfo(serviceKey, dataType, x, y,
                baseDate, baseTime, BASE_PAGE, NCST_PAGE_SIZE);

        return parsingJsonObject(result, FcstAPICommonListResponse.class);
    }

    /**
     * 기상청 초단기 예보
     */
    public FcstAPICommonListResponse getUltraForecast(int x, int y, String baseDate, String baseTime) {

        String result = dataGoKrApi.getUltraForecastInfo(serviceKey, dataType, x, y,
                baseDate, baseTime, BASE_PAGE, FCST_PAGE_SIZE);

        return parsingJsonObject(result, FcstAPICommonListResponse.class);
    }
}
