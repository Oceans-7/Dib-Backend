package com.oceans7.dib.global.api.http;

import com.oceans7.dib.global.api.response.kakaoAuth.OpenKeyListResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

public interface KakaoAuthApi {

    @GetExchange("/.well-known/jwks.json")
    OpenKeyListResponse getKakaoOpenKeyAddress();
}
