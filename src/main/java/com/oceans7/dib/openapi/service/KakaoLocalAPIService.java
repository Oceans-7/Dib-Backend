package com.oceans7.dib.openapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoLocalAPIService extends OpenAPIService {

    @Value("${open-api.kakao.service-key}")
    private String serviceKey;

    @Value("${open-api.kakao.header-prefix}")
    private String headerPrefix;

    @Value("${open-api.kakao.callback-url}")
    private String callbackUrl;

    @Value("${open-api.kakao.data-type}")
    private String dataType;
}
