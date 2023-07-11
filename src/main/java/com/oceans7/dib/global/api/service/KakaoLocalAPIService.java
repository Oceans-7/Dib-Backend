package com.oceans7.dib.global.api.service;

import com.oceans7.dib.global.api.http.KakaoApi;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KakaoLocalAPIService extends OpenAPIService {

    private final KakaoApi kakaoApi;

    public KakaoLocalAPIService(KakaoApi kakaoApi) { this.kakaoApi = kakaoApi; }

    @Value("${open-api.kakao.data-type}")
    private String dataType;

    /**
     * 쿼리 기반 주소 검색 api
     */
    public LocalResponse getSearchAddressLocalApi(String query) {
        String result = kakaoApi.getSearchAddress(dataType, query);

        return parsingJsonObject(result, LocalResponse.class);
    }

    /**
     * 좌표 기반 주소 검색 api
     */
    public LocalResponse getGeoAddressLocalApi(double x, double y) {
        String result = kakaoApi.getGeoAddress(dataType, x, y);

        return parsingJsonObject(result, LocalResponse.class);
    }

}
