package com.oceans7.dib.global.api.service;

import com.oceans7.dib.global.api.http.KakaoApi;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoLocalAPIService extends OpenAPIService {

    private final KakaoApi kakaoApi;

    /**
     * 쿼리 기반 주소 검색 api
     */
    public LocalResponse getSearchAddressLocalApi(String query) {
        String result = kakaoApi.getSearchAddress(query);

        return parsingJsonObject(result, LocalResponse.class);
    }

    /**
     * 좌표 기반 주소 검색 api
     */
    public LocalResponse getGeoAddressLocalApi(double x, double y) {
        String result = kakaoApi.getGeoAddress(x, y);

        return parsingJsonObject(result, LocalResponse.class);
    }

}
