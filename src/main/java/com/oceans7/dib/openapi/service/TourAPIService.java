package com.oceans7.dib.openapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.global.util.EncoderUtil;
import com.oceans7.dib.openapi.dto.response.LocationBasedList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TourAPIService extends AbstractOpenAPIService {

    @Value("${open-api.data-go-kr.service-key}")
    private String serviceKey;

    @Value("${open-api.data-go-kr.callback-url}")
    private String callbackUrl;

    @Value("${open-api.data-go-kr.data-type}")
    private String dataType;

    @Value("${open-api.data-go-kr.mobile-os}")
    private String mobileOS;

    @Value("${open-api.data-go-kr.mobile-app}")
    private String mobileApp;

    // 위치 기반 서비스 API 호출
    public LocationBasedList fetchDataFromLocationBasedApi(double mapX, double mapY, String contentTypeId) {
        String api = "/locationBasedList1";
        int radius = 20000;

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&mapX=" + mapX +
                "&mapY=" + mapY +
                "&radius=" + radius +
                "&contentTypeId=" + contentTypeId;

        String result = connectApi(urlStr);
        return parsingJsonObject(result);
    }

    // 키워드 기반 서비스 API 호출
    public LocationBasedList fetchDataFromSearchKeywordApi(String keyword) {
        String api = "/searchKeyword1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&keyword=" + EncoderUtil.toURLEncodeUtf8(keyword);

        String result = connectApi(urlStr);
        return parsingJsonObject(result);
    }

    @Override
    LocationBasedList parsingJsonObject(String json) {
        LocationBasedList result = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(json, LocationBasedList.class);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
