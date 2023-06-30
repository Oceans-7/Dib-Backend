package com.oceans7.dib.openapi.service;

import com.oceans7.dib.openapi.dto.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.openapi.dto.response.fcstapi.GetWeatherDigitalForecast;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class FcstInfoAPIService extends AbstractOpenAPIService {

    @Value("${open-api.data-go-kr.forecast-api.service-key}")
    private String serviceKey;

    @Value("${open-api.data-go-kr.forecast-api.callback-url}")
    private String callbackUrl;

    @Value("${open-api.data-go-kr.forecast-api.data-type}")
    private String dataType;



    public FcstAPICommonListResponse fetchDataFromNowForecast(double mapX, double mapY) {
        String api = "/getUltraSrtNcst";

        // 현재 시각
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = now.format(DateTimeFormatter.ofPattern("HH"));
        if(now.getMinute() < 40) {
            baseTime = now
                    .minusHours(1)
                    .format(DateTimeFormatter.ofPattern("HH"));
        }
        baseTime += "00";


        GetWeatherDigitalForecast.LatXLngY grid = new GetWeatherDigitalForecast().convertGRID_GPS(mapX, mapY);

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&dataType=" + dataType +
                "&nx=" + (int)grid.x +
                "&ny=" + (int)grid.y +
                "&base_date=" + baseDate +
                "&base_time=" + baseTime +
                "&pageNo=" + 1 +
                "&numOfRows=" + 8;

        String result = connectApi(urlStr);

        return parsingJsonObject(result, FcstAPICommonListResponse.class);
    }
}
